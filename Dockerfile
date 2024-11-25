# Use the official PHP image with Apache
FROM php:8.2-apache

# Install necessary PHP extensions
RUN docker-php-ext-install mysqli


# Enable Apache mod_rewrite
RUN a2enmod rewrite

# Set the working directory
WORKDIR /var/www/html

# Copy the current directory contents into the container
COPY . .

# Install Composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer
COPY xdebug.ini /usr/local/etc/php/conf.d/xdebug.ini
# Install PHP dependencies
RUN composer install

# Ensure the cache directory exists and set permissions for `film` and `search`
RUN mkdir -p /var/www/html/src/cache/films /var/www/html/src/cache/search \
    && chmod -R 777 /var/www/html/src/cache

# Expose port 80
EXPOSE 80

# Start Apache in the foreground
CMD ["apache2-foreground"]
