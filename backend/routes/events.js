const express = require('express');
const router = express.Router();
const db = require('../db');

// ===== GET /api/events =====
// Returns all events
router.get('/events', async (req, res) => {
  try {
    const [rows] = await db.query(`
      SELECT 
        E.ID AS id,
        E.TITLE AS name,
        E.DESCRIPTION AS description,
        DATE_FORMAT(E.EVENT_DATE, '%Y-%m-%d') AS date,
        E.PRICE AS price,
        E.TOTAL_SEATS AS totalSeats,
        E.AVAILABLE_SEATS AS seats,
        E.VENUE_ID AS venueId,
        E.CATEGORY_ID AS categoryId,
        E.ADMIN_ID AS adminId,
        V.NAME AS venueName,
        V.LOCATION AS venueLocation,
        C.NAME AS categoryName
      FROM EVENTS E
      LEFT JOIN VENUES V ON E.VENUE_ID = V.ID
      LEFT JOIN CATEGORY C ON E.CATEGORY_ID = C.ID
      ORDER BY E.ID
    `);
    res.json(rows);
  } catch (err) {
    console.error('Get events error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

// ===== POST /api/events =====
// Admin adds a new event
router.post('/events', async (req, res) => {
  try {
    const { name, date, price, seats, adminId } = req.body;

    if (!name || !date || price == null || seats == null) {
      return res.status(400).json({ error: 'Name, date, price, and seats are required' });
    }

    // Use default venue and category (ID 1)
    const venueId = req.body.venueId || 1;
    const categoryId = req.body.categoryId || 1;
    const admin = adminId || 1;

    const [result] = await db.query(
      `INSERT INTO EVENTS (TITLE, DESCRIPTION, EVENT_DATE, VENUE_ID, PRICE, TOTAL_SEATS, AVAILABLE_SEATS, CATEGORY_ID, ADMIN_ID)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
      [name, '', date, venueId, price, seats, seats, categoryId, admin]
    );

    res.status(201).json({
      message: 'Event created',
      id: result.insertId,
      name, date, price, seats
    });
  } catch (err) {
    console.error('Create event error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

// ===== PUT /api/events/:id =====
// Admin updates an event
router.put('/events/:id', async (req, res) => {
  try {
    const { id } = req.params;
    const { name, date, price, seats } = req.body;

    if (!name || !date || price == null || seats == null) {
      return res.status(400).json({ error: 'Name, date, price, and seats are required' });
    }

    const [result] = await db.query(
      `UPDATE EVENTS SET TITLE = ?, EVENT_DATE = ?, PRICE = ?, AVAILABLE_SEATS = ? WHERE ID = ?`,
      [name, date, price, seats, id]
    );

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Event not found' });
    }

    res.json({ message: 'Event updated', id: Number(id), name, date, price, seats });
  } catch (err) {
    console.error('Update event error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

// ===== DELETE /api/events/:id =====
// Admin deletes an event
router.delete('/events/:id', async (req, res) => {
  try {
    const { id } = req.params;

    const [result] = await db.query('DELETE FROM EVENTS WHERE ID = ?', [id]);

    if (result.affectedRows === 0) {
      return res.status(404).json({ error: 'Event not found' });
    }

    res.json({ message: 'Event deleted' });
  } catch (err) {
    console.error('Delete event error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;
