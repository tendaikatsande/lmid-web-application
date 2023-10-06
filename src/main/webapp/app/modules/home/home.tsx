import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';
import { FaCalendar, FaClipboardList, FaComments, FaDollarSign, FaEdit, FaTrash } from 'react-icons/fa';
import { useAppSelector } from 'app/config/store';
import { MapContainer, Marker, Popup, TileLayer, useMap } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import FilterForm from 'app/shared/layout/forms/filter-form';
export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const [selectedDate, setSelectedDate] = React.useState(null);

  const handleDateChange = event => {
    setSelectedDate(event.target.value);
  };

  const handleSubmit = event => {
    event.preventDefault();
    console.log('Selected Date is: ', selectedDate);
  };
  return (
    <Row>
      <Row>
        <Col>
          <form onSubmit={handleSubmit} className="col mt-3">
            <div className="form-group col-6">
              <label htmlFor="dateFilter">Date Filter</label>
              <input type="date" className="form-control" id="dateFilter" value={selectedDate || ''} onChange={handleDateChange} />
            </div>

            <button type="submit" className="btn btn-primary">
              Filter
            </button>
          </form>
        </Col>
      </Row>
      <Col>
        <h2>
          <Translate contentKey="home.title"></Translate>
        </h2>
        <p className="lead">
          <Translate contentKey="home.subtitle"></Translate>
        </p>
        {account?.login ? (
          <div>
            <MapContainer center={[-17.824858, 31.053028]} zoom={13} scrollWheelZoom={true} style={{ width: '100%', height: '700px' }}>
              {/* <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            /> */}
              <TileLayer
                url="https://stamen-tiles-{s}.a.ssl.fastly.net/toner-lite/{z}/{x}/{y}{r}.png"
                attribution="Map by <a href='http://stamen.com' target='_blank'>Stamen Design</a> | &copy; <a href='https://www.openstreetmap.org/copyright' target='_blank'>OpenStreetMap</a> contributors"
              />

              {/* <TileLayer
                url="https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Light_Gray_Base/MapServer/tile/{z}/{y}/{x}.png"
                attribution="Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ"
                maxZoom={16}
              /> */}
              {/* <TileLayer
                url="https://tiles.openseamap.org/seamark/{z}/{x}/{y}.png"
                attribution='Map data: &copy; <a href="http://www.openseamap.org">OpenSeaMap</a> contributors'
                maxZoom={16}
              /> */}

              <Marker position={[-17.824858, 31.053028]}>
                <Popup>
                  A pretty CSS3 popup. <br /> Easily customizable.
                </Popup>
              </Marker>
            </MapContainer>
          </div>
        ) : (
          <>
            <div>You are not logged in</div>
          </>
        )}
      </Col>
    </Row>
  );
};

export default Home;
