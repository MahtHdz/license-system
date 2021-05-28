const mongoose = require('mongoose');

/* DB Connection */
function dbConn(DBName){
  mongoose.connect('mongodb://localhost:27017/'+ DBName, {useNewUrlParser: true, useUnifiedTopology: true});
  //mongoose.Promise = global.Promise;
  var db = mongoose.connection;
  db.on('error', console.error.bind(console, 'MongoDB connection error:'));
  return db;
}

/* Data Schema */
var dataSchema = new mongoose.Schema({
  license:String,
  key:String
  //fullName:String,
  //emailAdress:String,
  //city:String,
  //coutry:String
});
var userModel = mongoose.model('users', dataSchema)


/* #################### CRUD OPERATIONS ################### */

/* CREATE */
async function insertUser(inputData, db){
  const result = await db.insertOne(inputData);
  console.log(`New User added. => id: ${result.insertedId}`);
}

/* READ */
async function findUser(userLicense, db){
  const result = await db.findOne({ license: userLicense });
    if (result) {
        console.log(`The user exist. => '${userLicense}':`);
        console.log(result);
        //console.log(result['_id']);
    } else {
        console.log(`User not found.`);
    }
}

/* UPDATE */
async function updateUser(newLicense, userLicense, db){
    const result = await db.updateOne({ license: userLicense }, { $set: { license: newLicense }});
    console.log(`${result.matchedCount} document(s) matched the query criteria.`);
    console.log(`${result.modifiedCount} document(s) was/were updated.`);
}

/* DELETE */
async function deleteUser(userLicense, db){
  const result = await db.deleteOne({ license: userLicense });
  console.log(`${result.deletedCount} document(s) was/were deleted.`);
}

const db = dbConn('licenseAPI').collection('users');
//insertUser({'license':'4123-23421-34-34-4', 'key':'dsrc1234eq34c23q4q2352343q2'}, db);
//findUser('4123-23421-34-34-4', db);
//updateUser('dsfa-543-gfdg-456g', '4123-23421-34-34-4', db);
findUser('dsfa-543-gfdg-456g', db);
deleteUser('dsfa-543-gfdg-456g', db);

