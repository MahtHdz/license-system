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
function insertUser(inputData){
  var userData = new userModel(inputData);
  userData.save();
}

/* READ */
function findUser(userLicense, callback){
    var userData = userModel.find({'license': userLicense});
  userData.exec();
}

/* UPDATE */
function updateUser(inputData, userLicense, callback){
  var userData = userModel.findOneAndUpdate({'license': userLicense}, inputData);
  userData.exec();
}

/* DELETE */
function deleteUser(userLicense, callback){
    var userData = userModel.findOneAndDelete({'license': userLicense});
  userData.exec();
}

function fetchData(){
  var userData = userModel.find({});
  userData.exec();
}

dbConn('LicenseAPI');
//insertUser({'license':'4123-23421-34-34-4', 'key':'dsrc1234eq34c23q4q2352343q2'});
fetchData();
//createDB("licenseAPI");
//createCollection("licenseAPI", "licenses");

