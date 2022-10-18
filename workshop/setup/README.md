# Setup [RabbitMQ](https://hub.docker.com/_/rabbitmq)
* Standalone
* Cluster

## Standalone

Start broker server
```
$docker container run -d --rm --hostname rabbit-01 --name rabbit-01 rabbitmq:3.11.1
```

Get Erlang cookie that can shared with other rabbitmq instance
```
$docker container exec -it rabbit-01 cat /var/lib/rabbitmq/.erlang.cookie
```

Clean up
```
$docker container stop rabbit-01
$docker container rm rabbit-01
```

Start with RabbitMQ Management
```
$docker container run -d --hostname rabbit-01 --name rabbit-01 \
   -p 5672:5672 -p 15672:15672 \
   -e RABBITMQ_ERLANG_COOKIE='cookie_for_clustering' \
   -e RABBITMQ_DEFAULT_USER=user \
   -e RABBITMQ_DEFAULT_PASS=password  \
   --name some-rabbit rabbitmq:3.11.1-management
```

Details
* port 5672 => RabbitMQ broker server
* port 15672 => Admin UI

### Manage user with command `rabbitmq-cli`
Add a new user
```
$rabbitmqctl add_user demo xyz
```
Tag user as administrator
```
$rabbitmqctl set_user_tags demo administrator
```
Set permission (read and write)
```
$rabbitmqctl set_permissions demo ".*" ".*" ".*"
```
List all users
```
$rabbitmqctl list_users
```
Change password
```
$rabbitmqctl change_password guest guest123
```

### Manage vhost
Create a new vhost
```
$rabbitmqctl add_vhost my-dev-vhost
```
List all vhosts
```
$rabbitmqctl list_vhosts
```
Set permission to user and vhost (read and write)
```
$rabbitmqctl set_permissions -p my-dev-vhost demo ".*" ".*" ".*"
```

## Cluster
* Rabbit Broker server 01
* Rabbit Broker server 02
* Rabbit Broker server 03
* [HAProxy server](https://hub.docker.com/_/haproxy) (Load Balancing)

Start Server
```
$docker-compose build
$docker-compose up -d
$docker-compose ps
```