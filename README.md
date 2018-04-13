# grafana-common

An easy-to-use Java client to manipulate Grafana via API calls.

## Current Features

* Support the creation, deletion, update and retrieval of the dashboards.
* Support YAML presentation of the dashboards.

More features to come.

## Build the package

The project is based on maven, the command below will generate the JAR package for further import and use:
```
mvn install
```

## Samples

### Sample YAML configuration of a dashboard

YAML is chosen as it is much more friendly to human read than JSON or XML. Besides, it is safe to ignore some settings
as default values are specified. These kind of settings will be listed in a later section.

```
title: Redis
datasource: local-prom
refresh: 30s
time:
  from: now-1h
  to: now
templating:
  - name: addr
    type: query
    datasource: local-prom
    query: label_values(redis_up, addr)
    refresh: 1
panels:
  - title: Uptime
    datasource: local-prom
    type: singlestat
    format: s
    decimals: 0
    gridPos:
      h: 7
      w: 4
      x: 0
      y: 0
    targets:
      - refId: A
        expr: redis_uptime_in_seconds{addr="$addr"}
  - title: Hits vs Misses / sec
    datasource: local-prom
    linewidth: 2
    decimals: 2
    legend:
      show: true
    gridPos:
      h: 7
      w: 8
      x: 16
      y: 0
    targets:
      - refId: A
        expr: irate(redis_keyspace_hits_total{addr="$addr"}[5m])
        legendFormat: hits
        intervalFactor: 2
      - refId: B
        expr: irate(redis_keyspace_misses_total{addr="$addr"}[5m])
        legendFormat: misses
        intervalFactor: 2
    xaxis:
      show: true
      mode: time
    yaxes:
      - format: short
        min: 0
      - format: short
  - title: Total Memory Usage
    datasource: local-prom
    linewidth: 2
    decimals: 2
    legend:
      show: true
    gridPos:
      h: 7
      w: 12
      x: 0
      y: 7
    targets:
      - refId: A
        expr: redis_memory_used_bytes{addr=~"$addr"}
        legendFormat: used
        intervalFactor: 2
      - refId: B
        expr: redis_memory_max_bytes{addr=~"$addr"}
        legendFormat: max
        intervalFactor: 2
    xaxis:
      show: true
      mode: time
    yaxes:
      - format: short
      - format: short
  - title: Total Items per DB
    datasource: local-prom
    fill: 7
    linewidth: 2
    stack: true
    nullPointMode: connected
    legend:
      show: true
      alignAsTable: true
      rightSide: true
      current: true
    gridPos:
      h: 7
      w: 12
      x: 0
      y: 14
    targets:
      - refId: A
        expr: sum (redis_db_keys{addr=~"$addr"}) by (db)
        legendFormat: "{{ db }}"
        intervalFactor: 2
    xaxis:
      mode: time
    yaxes:
      - format: short
      - format: short
```

### Code sample

First, initialize a Grafana object instance with the base url and the authentication string of the target Grafana server:
```
Grafana grafana = new Grafana("http://localhost:3000", "Bearer xxxxxx");
```

Read a YAML configuration file into the input stream and create a dashboard:
```
File yamlFile = new File("redis.yaml");
InputStream yamlStream = new FileInputStream(yamlFile);
DashboardSuccessfulPost resp = this.grafana.createDashboard(yamlStream);
```

The `DashboardSuccessfulPost` object contains information of the new dashboard.

With knowing the dashboard title you will be able to make changes to a dashboard. To update the dashboard, you may
simply update the YAML file and invoke the `updateDashboard` method. For deletion and retrieval, the information you
need is also the dashboard title, and sometimes, the name of the folder that contains the dashboard.

## Settings with default values

The list may change in the future

* schemaVersion: 1
* panel.fill: 1
* panel.linewidth: 1
* panel.lines: true
* panel.type: graph
* panel.xaxis.show: true
* panel.yaxes.logBase: 1
* panel.yaxes.show: true
* panel.thresholds[].color: critical
* panel.thresholds[].fill: true
* panel.thresholds[].line: true



