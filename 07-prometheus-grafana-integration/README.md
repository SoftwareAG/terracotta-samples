## About

Terracotta provides a list of key metrics in Prometheus compatible format over HTTP on the TMS (Terracotta Management Server) endpoint. Once you have deployed Prometheus, you can use [Grafana dashboards](https://grafana.com/docs/grafana/latest/dashboards/) for data visualizations and monitoring. To get started you can import the dashboard provided in this sample.

## Directory Structure

- [dashboard](./dashboard) - contains the dashboard file (JSON format).
- [images](./images) - contains dashboard screenshots with sample data.

## Dashboard

It contains following 3 rows:
- **Server Metrics** - contains server-side statistics.

   <img src="./images/server-metrics-row.png" width="80%" alt="server-metrics-row">
   
- **Cache Metrics** - contains Ehcache statistics.

   <img src="./images/cache-metrics-row.png" width="80%" alt="cache-metrics-row">

- **Store Metrics** - contains statistics for TCStore dataset operations.

   <img src="./images/store-metrics-row.png" width="80%" alt="store-metrics-row">
