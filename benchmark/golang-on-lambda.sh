cd ../golang-on-lambda/src/golang-on-lambda

for i in {1..100}
do 
    sls deploy -f hello --force > /dev/null
    curl -s -o /dev/null -w  "%{time_starttransfer}\n"  https://52h2t3nbij.execute-api.ap-northeast-1.amazonaws.com/dev/hello
done
