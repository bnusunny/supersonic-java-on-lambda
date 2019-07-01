// index.js

const serverless = require('serverless-http');
const express = require('express');
const app = express();

const AWS = require('aws-sdk');
AWS.config.update({region: 'ap-northeast-1'});

const ddb = new AWS.DynamoDB({apiVersion: '2012-08-10'});

app.get('/', function (req, res) {
  res.send('Hello World!');
})

app.post('/quote', function (req, res) {
  var params = {
    TableName: 'Quotes',
    Item: {
      'ID' : {S: '200'},
      'quote' : {S: 'My opinions may have changed, but not the fact that I’m right.'}
    }
  };
  
  // Call DynamoDB to add the item to the table
  ddb.putItem(params, function(err, data) {
    if (err) {
      console.log("Error", err);
      res.send("Error");
    } else {
      console.log("Success", data);
      res.send("Success");
    }
  });

}) 

module.exports.handler = serverless(app);