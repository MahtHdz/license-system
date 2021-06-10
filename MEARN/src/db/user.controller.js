/* 
axios.post('/', async (req, res) => {
    const {license, key, status} = req.body;
    const user = new User({license, key, status});
    await user.save();
    console.log(user);
    //res.json({status: "OK"});
});
*/