const mongoose = require('mongoose');
/* Data Schema */
/*  Future implementation 
const dataSchema = new Schema({
  license:{type: String, required: true},
  key:{type: String, required: true},
  fullName:String,
  emailAdress:String,
  city:String,
  coutry:String
}); 
*/
const dataSchema = new mongoose.Schema({
  license:{type: String, required: true},
  key:{type: String, required: true},
  status:{type:Boolean, required: true}
});
module.exports = mongoose.model('users', dataSchema);
