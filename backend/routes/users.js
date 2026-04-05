const express = require('express');
const router = express.Router();
const db = require('../db');

// ===== POST /api/login =====
// Authenticates user or admin based on role
router.post('/login', async (req, res) => {
  try {
    const { email, password, role } = req.body;

    if (!email || !password || !role) {
      return res.status(400).json({ error: 'Email, password, and role are required' });
    }

    if (role === 'admin') {
      // Check ADMIN table
      const [rows] = await db.query(
        'SELECT ID, NAME, EMAIL FROM ADMIN WHERE EMAIL = ? AND PASSWORD = ?',
        [email, password]
      );
      if (rows.length === 0) {
        return res.status(401).json({ error: 'Invalid admin credentials' });
      }
      return res.json({
        message: 'Login successful',
        userId: rows[0].ID,
        name: rows[0].NAME,
        email: rows[0].EMAIL,
        role: 'admin'
      });
    } else {
      // Check USERS table — auto-register if not found
      const [rows] = await db.query(
        'SELECT ID, NAME, EMAIL FROM USERS WHERE EMAIL = ? AND PASSWORD = ?',
        [email, password]
      );

      if (rows.length > 0) {
        // Existing user
        return res.json({
          message: 'Login successful',
          userId: rows[0].ID,
          name: rows[0].NAME,
          email: rows[0].EMAIL,
          role: 'user'
        });
      }

      // Check if email exists but password is wrong
      const [existingEmail] = await db.query(
        'SELECT ID FROM USERS WHERE EMAIL = ?',
        [email]
      );
      if (existingEmail.length > 0) {
        return res.status(401).json({ error: 'Incorrect password' });
      }

      // Auto-register new user
      const name = email.split('@')[0]; // Extract name from email
      const [result] = await db.query(
        'INSERT INTO USERS (NAME, EMAIL, PASSWORD) VALUES (?, ?, ?)',
        [name, email, password]
      );
      return res.json({
        message: 'Account created and logged in',
        userId: result.insertId,
        name: name,
        email: email,
        role: 'user'
      });
    }
  } catch (err) {
    console.error('Login error:', err);
    res.status(500).json({ error: 'Server error' });
  }
});

module.exports = router;
