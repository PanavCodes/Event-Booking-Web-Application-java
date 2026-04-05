const mysql = require('mysql2');

// Create a connection pool for better performance
const pool = mysql.createPool({
  host: 'localhost',
  user: 'root',
  password: 'panav',
  database: 'event_booking',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
});

// Export the promise-based pool for async/await usage
module.exports = pool.promise();
