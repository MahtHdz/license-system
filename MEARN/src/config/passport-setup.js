const passport = requiere('passport');
const GoogleStrategy = require( 'passport-google-oauth2' ).Strategy;

passport.use(new GoogleStrategy({
    clientID:     "24451185529-98s9d4d1gimn40vah0rlvidut5jr8do6.apps.googleusercontent.com",
    clientSecret: "d0xsEDvAwehsB0kxSeDXBEsR",
    callbackURL: "http://yourdomain:3000/auth/google/callback",
    passReqToCallback   : true
  },
  function(request, accessToken, refreshToken, profile, done) {
    User.findOrCreate({ googleId: profile.id }, function (err, user) {
      return done(err, user);
    });
  }
));