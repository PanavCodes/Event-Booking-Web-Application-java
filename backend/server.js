const express = require('express');
const cors = require('cors');
const path = require('path');
const db = require('./db');

const app = express();
const PORT = 3000;

// ===== Middleware =====
app.use(cors());
app.use(express.json());

// ===== Serve Frontend Static Files =====
app.use(express.static(path.join(__dirname, '..', 'frontend')));

// ===== Routes =====
app.use('/api', require('./routes/users'));
app.use('/api', require('./routes/events'));
app.use('/api', require('./routes/bookings'));

// ===== Health Check =====
app.get('/', (req, res) => {
  res.json({ message: 'Event Booking API is running' });
});

// ===== Seed Data on First Run =====
async function seedData() {
  try {
    // --- Seed default admin ---
    const [admins] = await db.query('SELECT COUNT(*) AS count FROM ADMIN');
    if (admins[0].count === 0) {
      await db.query(
        'INSERT INTO ADMIN (NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)',
        ['Admin', 'admin@gmail.com', 'admin123']
      );
      console.log('✅ Default admin seeded (admin@gmail.com / admin123)');
    }

    // --- Seed default user ---
    const [users] = await db.query('SELECT COUNT(*) AS count FROM USERS');
    if (users[0].count === 0) {
      await db.query(
        'INSERT INTO USERS (NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)',
        ['User', 'user@gmail.com', 'admin123']
      );
      console.log('✅ Default user seeded (user@gmail.com / admin123)');
    }

    // --- Seed default category ---
    const [categories] = await db.query('SELECT COUNT(*) AS count FROM CATEGORY');
    if (categories[0].count === 0) {
      await db.query(
        'INSERT INTO CATEGORY (NAME, DESCRIPTION) VALUES (?, ?)',
        ['General', 'General events category']
      );
      console.log('✅ Default category seeded');
    }

    // --- Seed default venue ---
    const [venues] = await db.query('SELECT COUNT(*) AS count FROM VENUES');
    if (venues[0].count === 0) {
      await db.query(
        'INSERT INTO VENUES (NAME, LOCATION, CAPACITY) VALUES (?, ?, ?)',
        ['Main Hall', 'City Center', 500]
      );
      console.log('✅ Default venue seeded');
    }

    // --- Seed sample events ---
    const [events] = await db.query('SELECT COUNT(*) AS count FROM EVENTS');
    if (events[0].count === 0) {
      // Get the IDs of the seeded venue, category, and admin
      const [[venue]] = await db.query('SELECT ID FROM VENUES LIMIT 1');
      const [[category]] = await db.query('SELECT ID FROM CATEGORY LIMIT 1');
      const [[admin]] = await db.query('SELECT ID FROM ADMIN LIMIT 1');

      const sampleEvents = [
        ['Music Concert',   'Live music event with top artists',      '2026-04-10', venue.ID, 500, 100, 100, category.ID, admin.ID],
        ['Tech Workshop',   'Hands-on technology workshop',           '2026-04-15', venue.ID, 300,  50,  50, category.ID, admin.ID],
        ['Startup Meetup',  'Networking event for startups',          '2026-04-18', venue.ID, 200,  80,  80, category.ID, admin.ID],
        ['Art Exhibition',  'Contemporary art showcase',              '2026-04-20', venue.ID, 150,  60,  60, category.ID, admin.ID],
        ['Coding Hackathon','24-hour coding competition',             '2026-04-25', venue.ID, 400,  40,  40, category.ID, admin.ID],
        ['Business Seminar','Business strategy and growth seminar',   '2026-04-28', venue.ID, 350,  70,  70, category.ID, admin.ID]
      ];

      for (const ev of sampleEvents) {
        await db.query(
          `INSERT INTO EVENTS (TITLE, DESCRIPTION, EVENT_DATE, VENUE_ID, PRICE, TOTAL_SEATS, AVAILABLE_SEATS, CATEGORY_ID, ADMIN_ID)
           VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
          ev
        );
      }
      console.log('✅ 6 sample events seeded');
    }
  } catch (err) {
    console.error('❌ Seed data error:', err.message);
  }
}

// ===== Start Server =====
app.listen(PORT, async () => {
  console.log(`🚀 Server running on http://localhost:${PORT}`);
  await seedData();
});
