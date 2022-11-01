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

haproxy            docker-entrypoint.sh hapro ...   Up             0.0.0.0:1936->1936/tcp,:::1936->1936/tcp, 0.0.0.0:5672->5672/tcp,:::5672->5672/tcp
rabbitmq-node-01   docker-entrypoint.sh bash  ...   Up (healthy)   15671/tcp, 0.0.0.0:15672->15672/tcp,:::15672->15672/tcp, 15691/tcp, 15692/tcp, 25672/tcp, 4369/tcp, 5671/tcp, 5672/tcp
rabbitmq-node-02   docker-entrypoint.sh bash  ...   Up (healthy)   15671/tcp, 0.0.0.0:15673->15672/tcp,:::15673->15672/tcp, 15691/tcp, 15692/tcp, 25672/tcp, 4369/tcp, 5671/tcp, 5672/tcp
rabbitmq-node-03   docker-entrypoint.sh bash  ...   Up (healthy)   15671/tcp, 0.0.0.0:15674->15672/tcp,:::15674->15672/tcp, 15691/tcp, 15692/tcp, 25672/tcp, 4369/tcp, 5671/tcp, 5672/tcp
```

### Check status of `node-01`
```
$docker container exec -it rabbitmq-node-01 bash -c "rabbitmqctl cluster_status"

Cluster status of node rabbit@rabbitmq-node-01 ...
Basics

Cluster name: rabbit@rabbitmq-node-01

Disk Nodes

rabbit@rabbitmq-node-01

Running Nodes

rabbit@rabbitmq-node-01
```

### Add `node-02` and `node-03` to the Cluster

Node 02
```
$docker container exec -it rabbitmq-node-02 bash -c "rabbitmqctl stop_app"
$docker container exec -it rabbitmq-node-02 bash -c "rabbitmqctl join_cluster rabbit@rabbitmq-node-01"
$docker container exec -it rabbitmq-node-02 bash -c "rabbitmqctl start_app"
```

Node 03
```
$docker container exec -it rabbitmq-node-03 bash -c "rabbitmqctl stop_app"
$docker container exec -it rabbitmq-node-03 bash -c "rabbitmqctl join_cluster rabbit@rabbitmq-node-01"
$docker container exec -it rabbitmq-node-03 bash -c "rabbitmqctl start_app"
```

### Check status of `node-01` again !!
```
$docker container exec -it rabbitmq-node-01 bash -c "rabbitmqctl cluster_status"

Running Nodes

rabbit@rabbitmq-node-01
rabbit@rabbitmq-node-02
rabbit@rabbitmq-node-03
```

## Config [HA (High Availability) mode](https://www.rabbitmq.com/ha.html) for queues

Mirror = 2
```
$docker container exec -it rabbitmq-node-01 bash -c 'rabbitmqctl set_policy ha-two "^some\." "{\"ha-mode\":\"exactly\",\"ha-params\":2,\"ha-sync-mode\":\"automatic\"}"'

```

Mirror all nodes
```
$docker container exec -it rabbitmq-node-01 bash -c 'rabbitmqctl set_policy ha-all "^all\_" "{\"ha-mode\":\"all\"}"'
```

## Check and Access to HAProxy
* URL = http://localhost:1936/haproxy?stats
  * user=haproxy
  * password=haproxy