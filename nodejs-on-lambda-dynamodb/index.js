// index.js

const { DynamoDB } = require('@aws-sdk/client-dynamodb-v2-node');
const ddb = new DynamoDB({ region: 'ap-northeast-1' });

module.exports.handler = (event, context, callback) => {
  var params = {
    TableName: 'Quotes',
    Item: {
      'ID': { S: '200' },
      'quote': { S: 'My opinions may have changed, but not the fact that I’m right.' }
    }
  };

  // Call DynamoDB to add the item to the table
  ddb.putItem(params, function (err, data) {
    if (err) {
      console.log("Error", err);
      callback(null, {
        statusCode: 400,
        headers: { 'Content-Type': 'text/plain' },
        body: 'Couldn\'t create the todo item.',
      });
    } else {
      console.log("Success", data);
      callback(null, {
        statusCode: 200,
        body: JSON.stringify(data),
      });
    }
  });
};