'use strict';
// Modules 
require('./db/conn');
const path = require('path');
const morgan = require('morgan');
const express = require('express');
//const passport = require('passport');

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
//app.use(passport.session());
//app.use(passport.initialize());

// Routes 
//app.use('/google', require('./routes/google.routes'));
app.use('/license', require('./routes/license.routes'));

// Static Files 
app.use(express.static(path.join(__dirname,'public')));

// Instance to run the server
app.listen(app.get('port'), () => {
   console.log(`Server on port ${app.get('port')}`);
});

module.exports = app;

// app.use(express.urlencoded({extended:false}));

