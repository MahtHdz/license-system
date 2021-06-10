const mongoose = require('mongoose');
const DB_URL = 'mongodb://localhost:27017/';
const DB = 'licenseAPI';
/* DB Connection */
mongoose.connect(DB_URL + DB, {useUnifiedTopology: true, useNewUrlParser: true})
  .then(client => console.log('DB is connected'))
  .catch(err => console.error(err));

module.exports = mongoose;