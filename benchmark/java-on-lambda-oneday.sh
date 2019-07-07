cd ../java-on-lambda

sls deploy -f hello --force > /dev/null
curl -s -o /dev/null -w  "%{time_starttransfer}\n" https://ywmwtn795m.execute-api.ap-northeast-1.amazonaws.com/dev

for i in {1..17280}
do 
    curl -s -o /dev/null -w  "%{time_starttransfer}\n" https://ywmwtn795m.execute-api.ap-northeast-1.amazonaws.com/dev
    sleep 5
done
