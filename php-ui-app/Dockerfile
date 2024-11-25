# Use the official PHP image with Apache
FROM php:8.1-apache

# Install Xdebug
RUN pecl install xdebug \
    && docker-php-ext-enable xdebug

# Copy custom Xdebug configuration
COPY xdebug.ini /usr/local/etc/php/conf.d/xdebug.ini

# Set the working directory
WORKDIR /var/www/html

# Copy the current directory contents into the container
COPY . .

# Expose port 80
EXPOSE 80

# Start Apache in the foreground
CMD ["apache2-foreground"]
