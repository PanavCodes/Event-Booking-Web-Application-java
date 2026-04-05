const express = require('express');
const router = express.Router();
const db = require('../db');

// ===== POST /api/bookings =====
// User creates a booking
router.post('/bookings', async (req, res) => {
  try {
    const { userId, eventId, tickets } = req.body;

    if (!userId || !eventId || !tickets) {
      return res.status(400).json({ error: 'userId, eventId, and tickets are required' });
    }

    // Get event details
    const [events] = await db.query(
      'SELECT ID, TITLE, EVENT_DATE, PRICE, AVAILABLE_SEATS FROM EVENTS WHERE ID = ?',
      [eventId]
    );

    if (events.length === 0) {
      return res.status(404).json({ error: 'Event not found' });
    }

    const event = events[0];

    if (tickets > event.AVAILABLE_SEATS) {
      return res.status(400).json({ error: 'Not enough seats available' });
    }

    const totalAmount = tickets * event.PRICE;

    // Insert booking
    const [result] = await db.query(
      'INSERT INTO BOOKINGS (USER_ID, EVENT_ID, QUANTITY, TOTAL_AMOUNT, STATUS) VALUES (?, ?, ?, ?, ?)',
      [userId, eventId, tickets, totalAmount, 'CONFIRMED']
    );

    // Decrement available seats
    await db.query(
      'UPDATE EVENTS SET AVAILABLE_SEATS = AVAILABLE_SEATS - ? WHERE ID = ?',
      [tickets, eventId]
    );

    // Return booking details for the payment page
    res.status(201).json({
      message: 'Booking created',
      bookingId: result.insertId,
      eventId: event.ID,
      eventName: event.TITLE,
      eventDate: event.EVENT_DATE,
      tickets: tickets,
      pricePerTicket: event.PRICE,
      totalPrice: totalAmount
    });
  } catch (err) {
    console.error('Create booking error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

// ===== GET /api/bookings/:userId =====
// Get all bookings for a user
router.get('/bookings/:userId', async (req, res) => {
  try {
    const { userId } = req.params;

    const [rows] = await db.query(`
      SELECT 
        B.ID AS bookingId,
        B.QUANTITY AS tickets,
        B.TOTAL_AMOUNT AS totalPrice,
        B.STATUS AS status,
        B.BOOKING_DATE AS bookedAt,
        E.TITLE AS eventName,
        DATE_FORMAT(E.EVENT_DATE, '%Y-%m-%d') AS eventDate,
        E.PRICE AS pricePerTicket
      FROM BOOKINGS B
      JOIN EVENTS E ON B.EVENT_ID = E.ID
      WHERE B.USER_ID = ?
      ORDER BY B.BOOKING_DATE DESC
    `, [userId]);

    res.json(rows);
  } catch (err) {
    console.error('Get bookings error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

// ===== POST /api/payments =====
// Store payment details
router.post('/payments', async (req, res) => {
  try {
    const { bookingId, paymentMethod } = req.body;

    if (!bookingId || !paymentMethod) {
      return res.status(400).json({ error: 'bookingId and paymentMethod are required' });
    }

    // Check that booking exists
    const [bookings] = await db.query('SELECT ID FROM BOOKINGS WHERE ID = ?', [bookingId]);
    if (bookings.length === 0) {
      return res.status(404).json({ error: 'Booking not found' });
    }

    // Insert payment
    const [result] = await db.query(
      'INSERT INTO PAYMENTS (BOOKING_ID, PAYMENT_STATUS, PAYMENT_METHOD) VALUES (?, ?, ?)',
      [bookingId, 'SUCCESS', paymentMethod]
    );

    res.status(201).json({
      message: 'Payment recorded',
      paymentId: result.insertId,
      bookingId: bookingId,
      paymentMethod: paymentMethod,
      status: 'SUCCESS'
    });
  } catch (err) {
    console.error('Create payment error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;
