# LNQ

Commands an utils for private server.

## Commands

`/suicide` and `/epic` - KYS
`/tps` - get current server TPS

## Player List Info

Displays TPS + server playercount on Player List

## API

Port `8000`

### `GET /`

Api landing page.

### `GET /data` -> `application/json`

```json
{
  "time": 1596232005289, // UNIX millis timestamp when requets recieved.
  "tick": {
    "tps": 20, // Current TPS to 1dp
    "time": 3.42 // Current tick time in ms to 2dp
  },
  "players": {
    // Self explanatory I hope
    "online": 4,
    "max": 20
  }
}
```
