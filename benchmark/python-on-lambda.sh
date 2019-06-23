cd ../python-on-lambda

for i in {1..100}
do 
    sls deploy -f hello --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n"  https://py4n74vs37.execute-api.ap-northeast-1.amazonaws.com/dev/hello
done
