import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

import _ from 'lodash';
import React from 'react';
import { render } from 'react-dom';

const client = require('./client');
import MonacoEditor from 'react-monaco-editor';

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            validations: [],
            filename: '/Users/mmaletich/git/baleen-product-demo/presentation_samples/purses.csv',
            nav: 'validation',
            config: '# Baleen Config'
        };

        this.navClick = this.navClick.bind(this)
    }

    getValidations() {
        client({method: 'GET', path: '/api/products/validations',
                params: {filename: this.state.filename}}).done(response => {
            let valids = response.entity.filter((validationResult) => validationResult.type !== undefined);
            if (this.state.config !== "# Baleen Config" && this.state.filename === '/Users/mmaletich/git/baleen-product-demo/presentation_samples/purses.cs') {
              // TODO hacky, hack this fake ignores errors.  replace with actual ignore.
              valids = valids.map((v) => {v.type = 'Success'; return v})
            }

            this.setState({validations: valids})
        })
    }

    componentDidMount() {
        this.getValidations();
        this.interval = setInterval(this.getValidations.bind(this), 1000);
    }

    navClick(nav) {
        return () => this.setState({nav: nav})
    }

    onConfigChange(newConfig) {
      this.setState({config: newConfig})
    }

    dataTraceYaml(validation) {
      return validation.dataTrace.stack.map((x) => "      - " + x).join("\n")
    }

    onAutoGenConfig() {
      // TODO escape
      const errors = _.sortBy(this.state.validations.filter((v) => v.type === "Error"), (x) => x.message);
      const todo = errors.map((validation) => "  - test: " + validation.message + "\n    dataTrace:\n" + this.dataTraceYaml(validation)).join("\n")

      this.setState({config: "# Baleen Config\n\nTODO:\n" + todo})
    }

    onFilenameChange(event) {
      this.setState({filename: event.target.value})
    }

    render() {
        return (
            <div>
                <Nav active={this.state.nav} onClick={this.navClick}/>
                <div className="container">
                  <label>Filename:</label>
                  <input id="filename" value={this.state.filename} onChange={this.onFilenameChange.bind(this)}/>
                </div>
                {this.state.nav === 'validation' &&
                    <div>
                        <ValidationMessages validations={this.state.validations}/>
                        <ValidationList productCtxs={this.state.validations}/>
                    </div>
                }
                {this.state.nav === 'configuration' &&
                <Configuration config={this.state.config} onChange={this.onConfigChange.bind(this)}
                 onAutoGenConfig={this.onAutoGenConfig.bind(this)}/>}
            </div>
        )
    }
}

class Nav extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <a className="navbar-brand" href="#">Baleen</a>
                <div className="collapse navbar-collapse">
                    <ul className="navbar-nav">
                        <li className={"nav-item " + this.props.active === 'validation' ? "active" : ""}>
                            <a className="nav-link" onClick={this.props.onClick("validation")}>Data</a>
                        </li>
                        <li className={"nav-item " + this.props.active === 'configuration' ? "active" : ""}>
                            <a className="nav-link" onClick={this.props.onClick("configuration")}>Configuration</a>
                        </li>
                    </ul>
                </div>
            </nav>
        )
    }
}

class Configuration extends React.Component {
  editorDidMount(editor, monaco) {
    editor.focus();
  }

  render() {
    const options = {
        selectOnLineNumbers: true
    };

    return <div className="container">
        <button type="button" className="btn btn-primary" onClick={this.props.onAutoGenConfig}>Auto Gen Config</button>
        <div className="editor">
          <MonacoEditor
           width="800"
           height="500"
           language="yaml"
           value={this.props.config}
           options={options}
           onChange={this.props.onChange}
           editorDidMount={this.editorDidMount}
          />
        </div>
    </div>
  }
}

class ValidationMessages extends React.Component {
    summarize(validation) {
        return validation.dataTrace.stack[validation.dataTrace.stack.length - 1] + " with value \"" + validation.value + "\" " + validation.message
    }

    render() {
        const messages = this.props.validations
            .filter((x) => x.type === 'Error')
            .map(validation => {
                    return (<div className="alert alert-danger">{this.summarize(validation)} (line {validation.dataTrace.tags.line})</div>)
                    // const dataTrace = validation.dataTrace.join(", ");
                    // return (<div className="alert alert-danger">{dataTrace} {validation.message}</div>)
                }
            );

        return (
            <div className="container">
                {messages}
            </div>
        )
    }
}

class ValidationMessage extends React.Component {
    render() {
        const dataTrace = props.validation.dataTrace;
        return <div className="alert alert-danger">{dataTrace} {props.validation.message}</div>;
    }
}


class ProductList extends React.Component{
    render() {
        const products = this.props.productCtxs.map(productCtx =>
            <Product key={productCtx.data.unique_id} productCtx={productCtx}/>
        );
        return (
            <div className="container">
                <div className="row">
                {products}
                </div>
            </div>
        )
    }
}

class ValidationList extends React.Component{
    render() {
        const toProducts = (list) => list.map(productCtx =>
            <Product key={productCtx.data.unique_id} dataTrace={productCtx.dataTrace} data={productCtx.data} />
        );
        const validProducts = toProducts(this.props.productCtxs.filter((x) => x.type === 'Success'));
        const invalidProducts = toProducts(this.props.productCtxs.filter((x) => x.type === 'Error'));
        return (
            <div className="container">
                <div className="row">
                    Invalid Products
                </div>
                <div className="row">
                    {invalidProducts}
                </div>
                <div className="row">
                    Valid Products
                </div>
                <div className="row">
                    {validProducts}
                </div>
            </div>
        )
    }
}

class Product extends React.Component{
    render() {
        let imageUrl;
        try {
            imageUrl = JSON.parse(this.props.data.image)[0];
        } catch(err) {
            imageUrl = "";
        }
        const dataTrace = this.props.dataTrace.stack.join(", ");
        const price = this.props.data.retail_price;
        return (
            <div className="col-md-3">
                <div className="card mb-4 box-shadow">
                    <div className="card-img-top">
                      <div className="reframe">
                        <img src={imageUrl} />
                      </div>
                    </div>
                    <div className="card-body">
                        <div>{this.props.data.product_name}</div>
                        <div>${price}</div>
                        <div className="dataTrace">{dataTrace}</div>
                    </div>
                </div>
            </div>
        )
    }
}

render(
    <App />,
    document.getElementById('react')
);
