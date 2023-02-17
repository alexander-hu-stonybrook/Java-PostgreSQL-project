/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cse532project2;

import java.sql.*;
import java.util.*;

/**
 *
 * @author engine
 */
public class QueryToList {

    private final String url = "jdbc:postgresql://localhost/CSE532";
    private final String user = "postgres";
    private final String password = "cse532";
    
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }
    
    public List<String> getQuery1() {

        String SQL = 
            "SELECT c.name AS CompanyName " +
            "FROM " +
            "companies c, " +
            "people p, " +
            "unnest(p.personowns) AS po " +
            "WHERE p.pid = ANY(c.boardheads) AND po.cid = c.cid AND po.sharesowned > 0";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            List<String> retlist = displayQuery1(rs);
            return retlist;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Collections.emptyList();
    }
    
    private List<String> displayQuery1(ResultSet rs) throws SQLException {
        List<String> retlist = new ArrayList<String>();
        while (rs.next()) {
            //System.out.println(rs.getString("CompanyName"));
            retlist.add(rs.getString("CompanyName"));
        }
        return retlist;
    }
    
    public List<ArrayList<String>> getQuery2() {

        String SQL = 
            "SELECT " +
            "p.name AS PersonName, " +
            "sum(c.shareprice * po.sharesowned) AS NetWorth " +
            "FROM " +
            "companies c, " +
            "people p, " +
            "unnest(p.personowns) AS po " +
            "WHERE c.cid = po.cid AND po.sharesowned > 0 " +
            "GROUP BY p.name";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            List<ArrayList<String>> retlist = displayQuery2(rs);
            return retlist;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Collections.emptyList();
    }
    
    private List<ArrayList<String>> displayQuery2(ResultSet rs) throws SQLException {
        List<ArrayList<String>> retlist = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> datalist = new ArrayList<>(Arrays.asList(rs.getString("PersonName"),rs.getString("NetWorth")));
            retlist.add(datalist);
            /*System.out.println(rs.getString("PersonName") + "\t"
                    + rs.getString("NetWorth"));*/
        }
        return retlist;
    }
    
    public List<ArrayList<String>> getQuery3() {

        String SQL = 
            "SELECT " +
            "    c.name AS CompanyName, " +
            "    p.name AS PersonWithMostShares, " +
            "    MAX(po.sharesowned) AS SharesOwned " +
            "FROM\n" +
            "    companies c, " +
            "    people p, " +
            "    unnest(p.personowns) AS po " +
            "WHERE\n" +
            "    p.pid = ANY(c.boardheads) AND " +
            "    c.cid = po.cid AND " +
            "    po.sharesowned > 0 " +
            "GROUP BY c.name, p.name";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            List<ArrayList<String>> retlist = displayQuery3(rs);
            return retlist;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Collections.emptyList();
    }
    
    private List<ArrayList<String>> displayQuery3(ResultSet rs) throws SQLException {
        List<ArrayList<String>> retlist = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> datalist = new ArrayList<>(
                    Arrays.asList(rs.getString("CompanyName"),rs.getString("PersonWithMostShares"))
            );
            retlist.add(datalist);
            /*System.out.println(rs.getString("PersonName") + "\t"
                    + rs.getString("NetWorth"));*/
        }
        return retlist;
    }
    
    public List<ArrayList<String>> getQuery4() {

        String SQL = 
            "SELECT " +
            "    comp1.name AS DominatingCompany, comp2.name AS DominatedCompany " +
            "FROM " +
            "    companies comp1, " +
            "    companies comp2 " +
            "WHERE " +
            "    EXISTS ( " +
            "        SELECT * " +
            "        FROM " +
            "            unnest(comp1.industry) i1, " +
            "            unnest(comp2.industry) i2 " +
            "        WHERE\n" +
            "            comp1.cid != comp2.cid AND " +
            "            i1 = i2 " +
            "    ) " +
            "EXCEPT " +
            "SELECT comp1.name, comp2.name " +
            "FROM " +
            "    companies comp1, " +
            "    companies comp2, " +
            "    people p2, " +
            "    unnest(p2.personowns) po2 " +
            "WHERE " +
            "    comp1.cid != comp2.cid AND " +
            "    p2.pid = ANY(comp2.boardheads) AND " +
            "    po2.sharesowned > ALL ( " +
            "        SELECT po1.sharesowned " +
            "        FROM " +
            "            people p1, " +
            "            unnest(p1.personowns) po1 " +
            "        WHERE " +
            "            p1.pid = ANY(comp1.boardheads) AND " +
            "            po1.cid = po2.cid " +
            "    )";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            List<ArrayList<String>> retlist = displayQuery4(rs);
            return retlist;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Collections.emptyList();
    }
    
    private List<ArrayList<String>> displayQuery4(ResultSet rs) throws SQLException {
        List<ArrayList<String>> retlist = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> datalist = new ArrayList<>(
                    Arrays.asList(rs.getString("DominatingCompany"),rs.getString("DominatedCompany"))
            );
            retlist.add(datalist);
            /*System.out.println(rs.getString("PersonName") + "\t"
                    + rs.getString("NetWorth"));*/
        }
        return retlist;
    }
    
        public List<ArrayList<String>> getQuery5() {

        String SQL = 
            "WITH RECURSIVE\n" +
            "    PersonDirectControl AS(\n" +
            "        SELECT\n" +
            "            p.pid AS pid,\n" +
            "            c.cid AS cid,\n" +
            "            (po.sharesowned::decimal / c.totalshares) AS PerOfcidOwned\n" +
            "        FROM\n" +
            "            people p,\n" +
            "            unnest(p.personowns) po,\n" +
            "            companies c\n" +
            "        WHERE\n" +
            "            po.sharesowned > 0 AND\n" +
            "            c.cid = po.cid\n" +
            "\n" +
            "    ),\n" +
            "    PersonIndirectControl (pid, cid1, cid2, PerOfcid1Owned, PerOfcid2Owned, path) AS(\n" +
            "        SELECT\n" +
            "            PDC.pid,\n" +
            "            c1.cid,\n" +
            "            c2.cid,\n" +
            "            PDC.PerOfcidOwned,\n" +
            "            ((PDC.PerOfcidOwned * co1.sharesowned) / c2.totalshares),\n" +
            "            ARRAY[c2.cid]\n" +
            "        FROM\n" +
            "            PersonDirectControl PDC,\n" +
            "            companies c1,\n" +
            "            companies c2,\n" +
            "            unnest(c1.companyowns) co1\n" +
            "        WHERE\n" +
            "            c1.cid = PDC.cid AND\n" +
            "            c1.cid != c2.cid AND\n" +
            "            co1.sharesowned > 0 AND\n" +
            "            c2.cid = co1.cid\n" +
            "      UNION ALL\n" +
            "        SELECT\n" +
            "            PIC.pid,\n" +
            "            c2.cid,\n" +
            "            c3.cid,\n" +
            "            PIC.PerOfcid2Owned,\n" +
            "            ((PIC.PerOfcid2Owned * co2.sharesowned) / c3.totalshares),\n" +
            "            path || c3.cid\n" +
            "        FROM\n" +
            "            PersonIndirectControl PIC,\n" +
            "            companies c2,\n" +
            "            companies c3,\n" +
            "            unnest(c2.companyowns) co2\n" +
            "        WHERE\n" +
            "            c2.cid = PIC.cid2 AND\n" +
            "            c2.cid != c3.cid AND\n" +
            "            co2.sharesowned > 0 AND\n" +
            "            c3.cid = co2.cid AND\n" +
            "            NOT (c3.cid = ANY(path))\n" +
            "    ),\n" +
            "    PersonSumIndirectControl AS(\n" +
            "        SELECT\n" +
            "            p.pid AS pid,\n" +
            "            c.cid AS cid,\n" +
            "            sum(PIC.PerOfcid2Owned) AS ic\n" +
            "        FROM\n" +
            "            PersonIndirectControl PIC,\n" +
            "            companies c,\n" +
            "            people p\n" +
            "        WHERE\n" +
            "            c.cid = PIC.cid2 AND\n" +
            "            p.pid = PIC.pid\n" +
            "        GROUP BY p.pid, c.cid\n" +
            "    ),\n" +
            "    PersonDirectAndIndirectControl AS(\n" +
            "        SELECT\n" +
            "            p.pid AS pid,\n" +
            "            c.cid AS cid,\n" +
            "            (PSIC.ic + PDC.PerOfcidOwned) AS dic\n" +
            "        FROM\n" +
            "            people p,\n" +
            "            companies c,\n" +
            "            PersonSumIndirectControl PSIC,\n" +
            "            PersonDirectControl PDC\n" +
            "        WHERE\n" +
            "            p.pid = PSIC.pid AND\n" +
            "            PSIC.pid = PDC.pid AND\n" +
            "            PSIC.cid = PDC.cid AND\n" +
            "            c.cid = PSIC.cid\n" +
            "    ),\n" +
            "    PersonAllControl (pid, cid, percid) AS(\n" +
            "        SELECT * FROM PersonDirectControl\n" +
            "        UNION\n" +
            "        SELECT * FROM PersonSumIndirectControl\n" +
            "        UNION\n" +
            "        SELECT * FROM PersonDirectAndIndirectControl\n" +
            "    ),\n" +
            "    PersonMaxControl AS(\n" +
            "        SELECT\n" +
            "            PAC.pid AS pid,\n" +
            "            PAC.cid AS cid,\n" +
            "            MAX(PAC.percid) AS percid\n" +
            "        FROM\n" +
            "            PersonAllControl PAC\n" +
            "        GROUP BY\n" +
            "            PAC.pid, PAC.cid\n" +
            "    ),\n" +
            "    Query5 AS (\n" +
            "        SELECT\n" +
            "            p.name AS PersonName,\n" +
            "            c.name AS CompanyName,\n" +
            "            TRUNC(PMC.percid * 100,4) AS Percentage\n" +
            "        FROM\n" +
            "            people p,\n" +
            "            companies c,\n" +
            "            PersonMaxControl PMC\n" +
            "        WHERE\n" +
            "            p.pid = PMC.pid AND\n" +
            "            c.cid = PMC.cid AND\n" +
            "            PMC.percid * 100 > 10\n" +
            "    )\n" +
            "SELECT * FROM Query5";

        try (Connection conn = connect();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            // display actor information
            List<ArrayList<String>> retlist = displayQuery5(rs);
            return retlist;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Collections.emptyList();
    }
    
    private List<ArrayList<String>> displayQuery5(ResultSet rs) throws SQLException {
        List<ArrayList<String>> retlist = new ArrayList<ArrayList<String>>();
        while (rs.next()) {
            ArrayList<String> datalist = new ArrayList<>(
                    Arrays.asList(rs.getString("PersonName"),rs.getString("CompanyName"),rs.getString("Percentage"))
            );
            retlist.add(datalist);
            /*System.out.println(rs.getString("PersonName") + "\t"
                    + rs.getString("NetWorth"));*/
        }
        return retlist;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //QueryToList qtl = new QueryToList();
        //List<String> outlist = qtl.getQuery1();
        //List<String> outlist = qtl.query1;
        //System.out.print(outlist);
        
    }
    
}
