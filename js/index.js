/* Requirements */
const axios = require('axios');
const express = require('express');

/* INIT */
const app = express();

/* Settings */
app.use(express.urlencoded({extended:false}));

app.get('/', (req, res) =>{
    axios.get('http://www.google.com')
     .then(response => {
        // handle success
        console.log(response)
     })
})
app.listen(3000)
