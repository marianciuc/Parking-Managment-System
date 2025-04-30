FROM node:20-alpine

WORKDIR /app
COPY package*.json ./
RUN npm install --legacy-peer-deps

COPY . .
ENV DISABLE_ESLINT_PLUGIN=true
ENV NODE_ENV=development

EXPOSE 3000
CMD ["npm", "run", "dev"]