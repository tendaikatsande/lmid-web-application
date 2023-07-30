import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './intervention.reducer';

export const Intervention = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const interventionList = useAppSelector(state => state.intervention.entities);
  const loading = useAppSelector(state => state.intervention.loading);
  const totalItems = useAppSelector(state => state.intervention.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="intervention-heading" data-cy="InterventionHeading">
        <Translate contentKey="jhipsterApp.intervention.home.title">Interventions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterApp.intervention.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/intervention/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterApp.intervention.home.createLabel">Create new Intervention</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {interventionList && interventionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="jhipsterApp.intervention.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="jhipsterApp.intervention.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('targetArea')}>
                  <Translate contentKey="jhipsterApp.intervention.targetArea">Target Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('targetArea')} />
                </th>
                <th className="hand" onClick={sort('targetDate')}>
                  <Translate contentKey="jhipsterApp.intervention.targetDate">Target Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('targetDate')} />
                </th>
                <th className="hand" onClick={sort('achievedArea')}>
                  <Translate contentKey="jhipsterApp.intervention.achievedArea">Achieved Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('achievedArea')} />
                </th>
                <th className="hand" onClick={sort('costOfIntervention')}>
                  <Translate contentKey="jhipsterApp.intervention.costOfIntervention">Cost Of Intervention</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('costOfIntervention')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="jhipsterApp.intervention.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="jhipsterApp.intervention.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="jhipsterApp.intervention.type">Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterApp.intervention.project">Project</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterApp.intervention.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="jhipsterApp.intervention.ward">Ward</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {interventionList.map((intervention, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/intervention/${intervention.id}`} color="link" size="sm">
                      {intervention.id}
                    </Button>
                  </td>
                  <td>
                    {intervention.startDate ? (
                      <TextFormat type="date" value={intervention.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{intervention.targetArea}</td>
                  <td>
                    {intervention.targetDate ? (
                      <TextFormat type="date" value={intervention.targetDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{intervention.achievedArea}</td>
                  <td>{intervention.costOfIntervention}</td>
                  <td>
                    {intervention.createdDate ? <TextFormat type="date" value={intervention.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {intervention.lastModifiedDate ? (
                      <TextFormat type="date" value={intervention.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {intervention.type ? <Link to={`/intervention-type/${intervention.type.id}`}>{intervention.type.name}</Link> : ''}
                  </td>
                  <td>{intervention.project ? <Link to={`/project/${intervention.project.id}`}>{intervention.project.name}</Link> : ''}</td>
                  <td>
                    {intervention.location ? <Link to={`/district/${intervention.location.id}`}>{intervention.location.name}</Link> : ''}
                  </td>
                  <td>{intervention.ward ? <Link to={`/ward/${intervention.ward.id}`}>{intervention.ward.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/intervention/${intervention.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/intervention/${intervention.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/intervention/${intervention.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="jhipsterApp.intervention.home.notFound">No Interventions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={interventionList && interventionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Intervention;
