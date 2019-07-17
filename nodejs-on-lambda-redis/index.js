
const redis = require("redis");
const redis_url = `redis://${process.env.REDIS_HOST}:6379/0` || "redis://localhost:6379/0"

console.log(`redis url is ${redis_url}`);

module.exports.handler = (event, context, callback) => {

  console.log("handler begin");

  const client = redis.createClient(redis_url);

  client.set("nodejs", "hello world", (err, data) => {
    client.quit();
    console.log(`callback begin. err= ${err}, data=${data}`);

    if (err) {
      const response = {
        statusCode: 500,
        body: JSON.stringify({
          message: "write to redis failed\n",
        }),
      };
  
      callback(null, response);

    } else {
      const response = {
        statusCode: 200,
        body: JSON.stringify({
          message: "data is writen to redis\n",
        }),
      };
  
      callback(null, response);
    }

  });

};