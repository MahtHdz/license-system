'use strict';
 
const express = require('express');
const User = require('../models/user');

const router = express.Router();

/* Resquest all data collection */
router.get('/', async (req, res) => {
    const users = await User.find();
    res.json(users);
});

//  CRUD EXPRESS
// CREATE
router.post('/', async (req, res) => {
    const {license, key, status} = req.body;
    const user = new User({license, key, status});
    await user.save();
    console.log(user);
    res.json({status: "OK"});
});

// READ
router.get('/:license', async (req, res) => {
    const user = await User.find({license: req.params.license});
    res.json(user);
});

// UPDATE
router.put('/:license', async (req, res) => {
    const {license, status} = req.body;
    const user = {license, status};
    await User.updateOne({license: req.params.license}, user);
    res.json({status: 'OK'});
});

// DELETE
router.delete('/:license', async (req, res) => {
    await User.findOneAndDelete({license: req.params.license});
    res.json({status: 'OK'});
});

module.exports = router;