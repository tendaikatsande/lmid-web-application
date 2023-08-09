import './footer.scss';

import React from 'react';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <footer className="bg-light text-center text-lg-start footer page-content">

    <div className="text-center p-3" style={{ backgroundColor: 'rgba(0, 0, 0, 0.2)' }}>
      © 2023 Copyright:
      <a className="text-dark" href="#">
        Land Management Interventions Database (LMID)
      </a>
    </div>
  </footer>
);

export default Footer;



