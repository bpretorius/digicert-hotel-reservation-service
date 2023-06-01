FROM public.ecr.aws/amazoncorretto/amazoncorretto:17

ARG JAR_FILE=hotel-reservation-service-0.0.1-SNAPSHOT.jar
ENV JAR_FILE=${JAR_FILE}

COPY target/${JAR_FILE} /

ENTRYPOINT exec java -jar /${JAR_FILE} --spring.profiles.active=local
