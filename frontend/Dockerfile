# Etapa 1: build de Angular
FROM node:18 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

# Etapa 2: servir con Nginx
FROM nginx:1.25
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80