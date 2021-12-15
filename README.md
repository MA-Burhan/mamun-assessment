mvn clean package

docker build -t mamun-assessment-docker .

docker run -p 8080:8080 mamun-assessment-docker