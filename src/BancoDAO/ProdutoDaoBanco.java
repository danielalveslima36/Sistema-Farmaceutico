package BancoDAO;

import Banco.ConFactory;
import InterfaceDao.ProdutoDAO;
import Model.Produto;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProdutoDaoBanco implements ProdutoDAO {

    private ConFactory factory;

    public ProdutoDaoBanco(){
        factory = new ConFactory();
    }
    @Override
    public Set<Produto> getProduto() throws SQLException, ClassNotFoundException {
        try(Connection connection = factory.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM produto"
            );

            ResultSet resultSet = statement.executeQuery();
            Set<Produto> produtos = new HashSet<>();

            while (resultSet.next()){
                String codProduto = resultSet.getString("codProduto");
                String descricao = resultSet.getString("descricao");
                String fabricante = resultSet.getString("fabricante");
                Float precoUnitario = resultSet.getFloat("precoUnitario");
                LocalDate validade = resultSet.getDate("validade").toLocalDate();
                produtos.add(new Produto(descricao, validade, codProduto, precoUnitario, fabricante));
            }
            return produtos;
        }
    }

    @Override
    public boolean salvar(Produto produto) throws SQLException, ClassNotFoundException {
        try(Connection connection = factory.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Produto(codProduto, descricao, fabricante, precoUnitario, validade) " +
                            "VALUES(?, ?, ?, ?, ?)"
            );

            statement.setString(1, produto.getCodigoDeBarras());
            statement.setString(2, produto.getDecricao());
            statement.setString(3, produto.getFabricante());
            statement.setFloat(4, produto.getPrecoUnitario());
            statement.setDate(5, Date.valueOf(produto.getValidade()));

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deletar(Produto produto) throws SQLException, ClassNotFoundException {
        try(Connection connection = factory.getConnection()){
           PreparedStatement statement = connection.prepareStatement(
                   "DELETE FROM produto WHERE codProduto = ?"
           );

           statement.setString(1, produto.getCodigoDeBarras());
           return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Produto buscarPorCodigo(String codigoBarras) throws SQLException, ClassNotFoundException {
        try(Connection connection = factory.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM produto WHERE codProduto = ?"
            );

            statement.setString(1, codigoBarras);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                String codProduto = resultSet.getString("codProduto");
                String descricao = resultSet.getString("descricao");
                String fabricante = resultSet.getString("fabricante");
                Float precoUnitario = resultSet.getFloat("precoUnitario");
                LocalDate validade = resultSet.getDate("validade").toLocalDate();
                return new Produto(descricao, validade, codProduto, precoUnitario, fabricante);
            } else return null;
        }
    }

    @Override
    public boolean atualizar(Produto produto) throws SQLException, ClassNotFoundException {
        try(Connection connection = factory.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE produto" +
                            "SET descricao = ?, fabricante = ?, precoUnitario = ?, validade = ?" +
                            "WHERE codproduto = ?"
            );

            statement.setString(1, produto.getDecricao());
            statement.setString(2, produto.getFabricante());
            statement.setFloat(3, produto.getPrecoUnitario());
            statement.setDate(4, Date.valueOf(produto.getValidade()));
            statement.setString(5, produto.getCodigoDeBarras());

            return statement.executeUpdate() > 0;
        }
    }
}