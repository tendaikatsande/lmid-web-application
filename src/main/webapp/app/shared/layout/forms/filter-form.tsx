import React, { useState } from 'react';
import { FaExpand, FaFilter, FaPlusCircle, FaTrash } from 'react-icons/fa';

function FilterForm(props) {
  const [filterData, setFilterData] = React.useState([]);

  const [fieldData] = useState(props.initialFieldData);

  const handleInputChange = (index, event) => {
    const values = [...filterData];
    if (event.target.name === 'filterKey') {
      const selectedField = fieldData.find(field => field.fieldName === event.target.value);
      values[index].filterKey = event.target.value;
      values[index].fieldType = selectedField.fieldType;
    } else if (event.target.name === 'operation') {
      values[index].operation = event.target.value;
    } else {
      values[index].value = event.target.value;
    }
    setFilterData(values);
  };

  const handleAddFields = () => {
    setFilterData([
      ...filterData,
      {
        filterKey: '',
        operation: '',
        value: '',
        fieldType: '',
      },
    ]);
  };

  const handleRemoveFields = index => {
    const values = [...filterData];
    values.splice(index, 1);
    setFilterData(values);
  };

  const handleSubmit = e => {
    e.preventDefault();
    // Pass the form data up to the parent component
    props.onFormSubmit(filterData);
  };
  const getInputType = fieldType => {
    switch (fieldType) {
      case 'Integer':
      case 'BigDecimal':
        return 'number';
      case 'LocalDate':
      case 'Instant':
        return 'date';
      default:
        return 'text';
    }
  };

  return (
    <form className="">
      <div className="">
        {filterData.map((filterItem, index) => (
          <div className="row mb-2" key={`${filterItem}-${index}`}>
            <div className="form-group col-md-3">
              <select
                className="form-control"
                name="filterKey"
                value={filterItem.filterKey}
                onChange={event => handleInputChange(index, event)}
              >
                <option value="">Select filter key</option>
                {fieldData.map(field => (
                  <option key={field.fieldName} value={field.fieldName}>
                    {field.fieldName}
                  </option>
                ))}
              </select>
            </div>
            <div className="form-group col-md-3">
              <select
                className="form-control"
                name="operation"
                value={filterItem.operation}
                onChange={event => handleInputChange(index, event)}
              >
                <option value="eq">equal to</option>
                <option value="ne">not equal to</option>
                <option value="lt">less than</option>
                <option value="gt">greater than</option>
              </select>
            </div>
            <div className="form-group col-md-3">
              <input
                className="form-control"
                type={getInputType(filterItem.fieldType)}
                name="value"
                value={filterItem.value}
                onChange={event => handleInputChange(index, event)}
              />
            </div>
            <div className="form-group col-md-3">
              <button type="button" className="btn btn-danger btn-sm" onClick={() => handleRemoveFields(index)}>
                <FaTrash />
              </button>
            </div>
          </div>
        ))}

        <div className="">
          <button type="button" className="btn btn-primary btn-sm mr-3" onClick={() => handleAddFields()}>
            <FaPlusCircle /> Add fields
          </button>
          <span className="m-2"></span>
          <button type="button" className="btn btn-success btn-sm" onClick={handleSubmit}>
            <FaFilter /> filter
          </button>
        </div>
      </div>
    </form>
  );
}

export default FilterForm;
