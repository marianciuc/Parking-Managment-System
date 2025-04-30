FROM node:18-alpine AS build
WORKDIR /app
COPY . .
FROM nginx:alpine
COPY --from=build /app /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]