const express = require('express');
const https = require('https');
const app = express();
const port = process.env.PORT || 443;
const cors = require('cors');
const path = require('path');
const { readFileSync } = require('fs');

app.use(cors());
app.use(express.static('public'));

app.get('*', (req, res) => {
   res.sendFile(path.resolve(__dirname, 'public', 'index.html'));
});

const options = {
  cert: readFileSync('./stminakirchemuenchen_de.crt'),
  p7b: readFileSync('./stminakirchemuenchen_de.p7b'),
  key: readFileSync('./stminakirchemuenchen.de.key'),
  ca: readFileSync('./stminakirchemuenchen_de.ca-bundle')
};

var server = https.createServer(options, app).listen(port, function(){
  console.log("Express server listening on port " + port);
});