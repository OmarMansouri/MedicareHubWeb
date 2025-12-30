import React from "react";
import "../styles/Maps.css";
import L from "leaflet";
import {
  MapContainer,
  TileLayer,
  useMap,
  Marker,
  Popup,
  LayerGroup,
  Circle,
  FeatureGroup,
  rectangle,
} from "react-leaflet";
const position = [48.85, 2.35];
let DefaultIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconSize: [25, 41],
});
const center = [48.805652001165086, 2.4225076390203393];

const fillRedOptions = { fillColor: "red" };

export default function Maps() {
  return (
    <MapContainer center={[48.85, 2.35]} zoom={13} scrollWheelZoom={false}>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <Marker position={[48.7950125, 2.4436626]}>
        <Popup>You are Here</Popup>
      </Marker>
      <LayerGroup>
        <Circle center={center} pathOptions={fillRedOptions} radius={1000} />
        <Circle
          center={center}
          pathOptions={fillRedOptions}
          radius={100}
          stroke={false}
        />
      </LayerGroup>
    </MapContainer>
  );
}

L.Marker.prototype.options.icon = DefaultIcon;
