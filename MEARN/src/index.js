'use strict';
// Modules 
require('./db/conn');
const path = require('path');
const morgan = require('morgan');

/* 
################# 
####  INIT   ####    
################# */
const app = express();

// Settings 
const PORT = 3000;
app.set('port', process.env.PORT || PORT);
app.use(express.urlencoded({ extended: true }));

// Middlewares 
app.use(morgan('dev'));
app.use(express.json());

// Routes 
app.use('/license', require('./routes/license.routes'));

// Static Files 
app.use(express.static(path.join(__dirname,'public')));

// Instance to run the server
app.listen(app.get('port'), () => {
   console.log(`Server on port ${app.get('port')}`);
});

module.exports = app;

// app.use(express.urlencoded({extended:false}));

