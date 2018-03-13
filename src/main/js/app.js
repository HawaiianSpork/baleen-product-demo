const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            productCtxs: [],
            validations: [],
            nav: 'products'
        };

        this.navClick = this.navClick.bind(this)
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/products'}).done(response => {
            this.setState({productCtxs: response.entity})
        })

        client({method: 'GET', path: '/api/products/validations'}).done(response => {
            let valids = response.entity.filter((validationResult) => validationResult.type != undefined)
            this.setState({validations: valids})
        })
    }

    navClick(nav) {
        return () => this.setState({nav: nav})
    }

    render() {
        return (
            <div>
                <Nav onClick={this.navClick}/>
                {this.state.nav === 'products' &&
                    < ProductList productCtxs={this.state.productCtxs}/>}
                {this.state.nav === 'validation' &&
                    <ValidationList productCtxs={this.state.validations}/>}
            </div>
        )
    }
}

class Nav extends React.Component {
    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <div className="collapse navbar-collapse">
                    <ul className="navbar-nav">
                        <li className="nav-item active">
                            <a className="nav-link" onClick={this.props.onClick("products")}>Products</a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" onClick={this.props.onClick("validation")}>Validation</a>
                        </li>
                    </ul>
                </div>
            </nav>
        )
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
            <Product key={productCtx.context.data.unique_id} productCtx={productCtx.context}/>
        );
        const validProducts = toProducts(this.props.productCtxs.filter((x) => x.type === 'Success'));
        const invalidProducts = toProducts(this.props.productCtxs.filter((x) => x.type === 'Error'))
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
            imageUrl = JSON.parse(this.props.productCtx.data.image)[0];
        } catch(err) {
            imageUrl = "";
        }
        return (
            <div className="col-md-3">
                <div className="card mb-4 box-shadow">
                    <img className="card-img-top" src={imageUrl}/>
                    <div className="card-body">
                        {this.props.productCtx.data.product_name}
                        {this.props.productCtx.data.retail_price}
                    </div>
                </div>
            </div>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)