FROM gradle:8.3-jdk20

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app
