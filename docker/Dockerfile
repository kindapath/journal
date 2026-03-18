FROM nginx:alpine

COPY docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY webApp/build/dist/js/productionExecutable/ /usr/share/nginx/html/

EXPOSE 3000
