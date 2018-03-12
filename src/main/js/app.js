const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {productCtxs: []};
    }

    componentDidMount() {
        client({method: 'GET', path: '/api/products'}).done(response => {
            this.setState({productCtxs: response.entity});
    });
    }

    render() {
        return (
            <ProductList productCtxs={this.state.productCtxs}/>
    )
    }
}

class ProductList extends React.Component{
    render() {
        const products = this.props.productCtxs.map(productCtx =>
            <Product key={productCtx.data.unique_id} productCtx={productCtx}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>Name</th>
                    <th>Image</th>
                </tr>
                {products}
                </tbody>
            </table>
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
            <tr>
                <td>{this.props.productCtx.data.product_name}</td>
                <td><img src={imageUrl} height="50px" width="50px"/></td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)