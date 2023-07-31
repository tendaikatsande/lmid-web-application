import React, { useEffect, useState } from 'react';
import { Translate, translate, ValidatedField } from 'react-jhipster';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Alert, Row, Col, Form } from 'reactstrap';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { type FieldError, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import login from '../login/login';

export interface ILoginModalProps {
  showModal: boolean;
  loginError: boolean;
  handleLogin: (username: string, password: string, rememberMe: boolean) => void;
  handleClose: () => void;
}

export default function LoginPage() {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const showModalLogin = useAppSelector(state => state.authentication.showModalLogin);
  const [showModal, setShowModal] = useState(showModalLogin);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    setShowModal(true);
  }, []);

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });
  const handleLoginSubmit = (e: any) => {
    // dispatch(login(username, password, rememberMe));
  };
  return (
    <div className="container dflex justify-content-center align-items-center w-full">
      {' '}
      <Form onSubmit={handleLoginSubmit} className='col-4'>
        <Row>
          <Col md="12">
            {loginError ? (
              <Alert color="danger" data-cy="loginError">
                <Translate contentKey="login.messages.error.authentication">
                  <strong>Failed to sign in!</strong> Please check your credentials and try again.
                </Translate>
              </Alert>
            ) : null}
          </Col>
          <Col md="12">
            <ValidatedField
              name="username"
              label={translate('global.form.username.label')}
              placeholder={translate('global.form.username.placeholder')}
              required
              autoFocus
              data-cy="username"
              validate={{ required: 'Username cannot be empty!' }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
            />
            <ValidatedField
              name="password"
              type="password"
              label={translate('login.form.password')}
              placeholder={translate('login.form.password.placeholder')}
              required
              data-cy="password"
              validate={{ required: 'Password cannot be empty!' }}
              register={register}
              error={errors.password as FieldError}
              isTouched={touchedFields.password}
            />
            <ValidatedField
              name="rememberMe"
              type="checkbox"
              check
              label={translate('login.form.rememberme')}
              value={true}
              register={register}
            />
          </Col>
        </Row>
        <div className="mt-1">&nbsp;</div>
        <Alert color="warning">
          <Link to="/account/reset/request" data-cy="forgetYourPasswordSelector">
            <Translate contentKey="login.password.forgot">Did you forget your password?</Translate>
          </Link>
        </Alert>
        <Alert color="warning">
          <span>
            <Translate contentKey="global.messages.info.register.noaccount">You don&apos;t have an account yet?</Translate>
          </span>{' '}
          <Link to="/account/register">
            <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
          </Link>
        </Alert>

        <Button color="primary" type="submit" data-cy="submit">
          <Translate contentKey="login.form.button">Sign in</Translate>
        </Button>
      </Form>
    </div>
  );
}
