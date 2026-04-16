FROM nginx:alpine

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY index.html /usr/share/nginx/html/index.html
COPY styles.css /usr/share/nginx/html/styles.css
COPY favicon.png /usr/share/nginx/html/favicon.png
COPY images /usr/share/nginx/html/images
COPY downloads /usr/share/nginx/html/downloads

EXPOSE 3000