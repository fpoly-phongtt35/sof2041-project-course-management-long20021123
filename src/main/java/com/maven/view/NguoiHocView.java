/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.maven.view;

import com.maven.model.NguoiHoc;
import com.maven.service.NguoiHocService;
import com.maven.swing.EventPagination;
import com.maven.until.HibernateUtil;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author long0
 */
public class NguoiHocView extends javax.swing.JFrame {

    DefaultTableModel model;
    List<NguoiHoc> nguoiHocList = null;
    private int index = -1;
    NguoiHocService nhsv = new NguoiHocService();

    /**
     * Creates new form NguoiHocView
     */
    public NguoiHocView() {
        initComponents();
        pagination1.addEventPagination(new EventPagination() {
            @Override
            public void pageChanged(int page) {
                loadData(page);
            }
        });
    }

    void fillTable(List<NguoiHoc> list) {
        model.setRowCount(0);
        for (NguoiHoc nguoiHoc : nguoiHocList) {
            model.addRow(nguoiHoc.toData());
        }
    }

    NguoiHoc readForm() {
        String maNguoiHoc = txtMaNguoiHoc.getText();
        String matKhau = txtMatKhau.getText();
        boolean gioiTinh;
        if (rdNam.isSelected()) {
            gioiTinh = true;
        } else {
            gioiTinh = true;
        }
        Date ngaySinh = txtNgaySinh.getDate();
        String email = txtEmail.getText();
        String dienThoai = txtSoDienThoai.getText();
        String ghiChu = txtGhiChu.getText();
        String maNV = txtMaNhanVien.getText();
        Date ngayDK = txtNgayDangKi.getDate();
        return new NguoiHoc(maNguoiHoc, matKhau, gioiTinh, ngaySinh, email, dienThoai, ghiChu, maNV, ngayDK);
    }

    boolean validateData() {
        if (txtMaNguoiHoc.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã người học không được để trống");
            txtMaNguoiHoc.requestFocus();
            return false;
        } else {
            if (txtMaNguoiHoc.getText().length() > 7) {
                JOptionPane.showMessageDialog(this, "Mã người học không quá 7 kí tự");
                txtMaNguoiHoc.requestFocus();
                return false;
            }
        }

        if (txtMaNhanVien.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống");
            txtMaNhanVien.requestFocus();
            return false;
        } else {
            if (txtMaNhanVien.getText().length() > 20) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không được quá 20 kí tự");
                txtMaNhanVien.requestFocus();
                return false;
            }
        }

        if (txtMatKhau.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống");
            txtMatKhau.requestFocus();
            return false;
        } else {
            if (txtMatKhau.getText().length() > 50) {
                JOptionPane.showMessageDialog(this, "Mật khẩu không quá 50 kí tự");
                txtMatKhau.requestFocus();
                return false;
            }
        }

        if (txtNgaySinh.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn ngày sinh");
            return false;
        }

        if (txtNgayDangKi.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn ngày đăng kí");
            return false;
        }

        if (txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email không được để trống");
            txtEmail.requestFocus();
            return false;
        } else {
            if (!(txtEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
                JOptionPane.showMessageDialog(this, "Email sai định dạng");
                txtEmail.requestFocus();
                return false;
            }
            if (txtEmail.getText().length() > 50) {
                JOptionPane.showMessageDialog(this, "Email không quá 50 kí tự");
                txtEmail.requestFocus();
                return false;
            }
        }
        if (txtSoDienThoai.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại  không được để trống");
            txtSoDienThoai.requestFocus();
            return false;
        } else {
            if (!(txtSoDienThoai.getText().matches("^\\+?[0-9\\s\\-]+$"))) {
                JOptionPane.showMessageDialog(this, "Số điện thoại sai định dạng");
                txtSoDienThoai.requestFocus();
                return false;
            }
            if (txtSoDienThoai.getText().length() > 10) {
                JOptionPane.showMessageDialog(this, "Số điện thoại không được quá 10 số thứ tự");
                txtSoDienThoai.requestFocus();
                return false;
            }
        }

        if (txtGhiChu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ghi chú không được để trống");
            txtGhiChu.requestFocus();
            return false;
        } else {
            if (txtGhiChu.getText().length() > 225) {
                JOptionPane.showMessageDialog(this, "Ghi chú không được quá 225 kí tự");
                txtGhiChu.requestFocus();
                return false;
            }
        }

        return true;
    }

    void showData(int index) {
        NguoiHoc nh = nguoiHocList.get(index);
        txtMaNguoiHoc.setText(nh.getMaNH());
        txtEmail.setText(nh.getEmail());
        txtGhiChu.setText(nh.getGhiChu());
        txtMaNhanVien.setText(nh.getMaNV());
        txtSoDienThoai.setText(nh.getDienThoai());
        txtNgaySinh.setDate(nh.getNgaySinh());
        txtNgayDangKi.setDate(nh.getNgayDK());
        txtMatKhau.setText(nh.getMatKhau());
        if (nh.getGioiTinh()) {
            rdNam.setSelected(true);
        } else {
            rdNu.setSelected(true);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        textArea1 = new com.maven.swing.TextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMaNguoiHoc = new com.maven.swing.TextField();
        txtEmail = new com.maven.swing.TextField();
        rdNam = new com.maven.swing.RadioButtonCustom();
        jLabel4 = new javax.swing.JLabel();
        rdNu = new com.maven.swing.RadioButtonCustom();
        txtMatKhau = new com.maven.swing.PasswordField();
        btnHide = new com.maven.swing.Button();
        jLabel5 = new javax.swing.JLabel();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        txtSoDienThoai = new com.maven.swing.TextField();
        txtNgayDangKi = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        textAreaScroll1 = new com.maven.swing.TextAreaScroll();
        txtGhiChu = new com.maven.swing.TextArea();
        txtMaNhanVien = new com.maven.swing.TextField();
        btnThem = new com.maven.swing.Button();
        btnSua = new com.maven.swing.Button();
        btnXoa = new com.maven.swing.Button();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbNguoiHoc = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        pagination1 = new com.maven.swing.Pagination();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("QUẢN LÍ NHÂN VIÊN");
        jLabel1.setToolTipText("");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("QUẢN LÍ NHÂN VIÊN");
        jLabel2.setToolTipText("");

        textArea1.setColumns(20);
        textArea1.setRows(5);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("QUẢN LÍ NGƯỜI HỌC");

        txtMaNguoiHoc.setLabelText("Mã người học");

        txtEmail.setLabelText("Email");
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });

        rdNam.setBackground(new java.awt.Color(255, 204, 0));
        buttonGroup1.add(rdNam);
        rdNam.setSelected(true);
        rdNam.setText("Nam");
        rdNam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdNamActionPerformed(evt);
            }
        });

        jLabel4.setText("Giới Tính ");

        buttonGroup1.add(rdNu);
        rdNu.setText("Nữ");

        txtMatKhau.setLabelText("Mật Khẩu");

        btnHide.setText("Hide");
        btnHide.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnHide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHideActionPerformed(evt);
            }
        });

        jLabel5.setText("Ngày Sinh");

        txtNgaySinh.setDateFormatString("dd-MM-yyyy");

        txtSoDienThoai.setLabelText("Điện thoại");

        txtNgayDangKi.setDateFormatString("dd-MM-yyyy");

        jLabel6.setText("Ngày Đăng Kí");

        textAreaScroll1.setLabelText("Ghi chú");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        textAreaScroll1.setViewportView(txtGhiChu);

        txtMaNhanVien.setLabelText("Mã nhân viên");

        btnThem.setText("Thêm ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");

        btnXoa.setText("Xóa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(272, 272, 272))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(242, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(textAreaScroll1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNgayDangKi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(txtSoDienThoai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(24, 24, 24))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(28, 28, 28)))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                            .addGap(12, 12, 12)
                                            .addComponent(rdNam, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(71, 71, 71)
                                            .addComponent(rdNu, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGap(183, 183, 183))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtMaNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(btnHide, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(87, 87, 87)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(105, 105, 105)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(147, 147, 147))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtMaNguoiHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHide, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdNam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(rdNu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel6))
                    .addComponent(txtNgayDangKi, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textAreaScroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(450, 450, 450))
        );

        jtbNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã NH", "Mã NV", "Giới tính", "Ngày sinh", "Email", "Mật khẩu", "Điện thoại", "Ghi Chú", "Ngày ĐK"
            }
        ));
        jtbNguoiHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbNguoiHocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtbNguoiHoc);
        if (jtbNguoiHoc.getColumnModel().getColumnCount() > 0) {
            jtbNguoiHoc.getColumnModel().getColumn(8).setResizable(false);
        }

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));

        pagination1.setOpaque(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pagination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(696, 696, 696))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHideActionPerformed
        char echoChar = txtMatKhau.getEchoChar();
        if (echoChar == '\u0000') {
            txtMatKhau.setEchoChar('*');
            btnHide.setText("Hiện");
        } else {
            txtMatKhau.setEchoChar('\u0000');
            btnHide.setText("Ẩn");
        }
    }//GEN-LAST:event_btnHideActionPerformed

    private void rdNamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdNamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdNamActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed
    private void loadData(int page) {

        int limit = 10;
        int offset = (page - 1) * limit;

        try {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            DecimalFormat f = new DecimalFormat("#,##0.##");
            model = (DefaultTableModel) jtbNguoiHoc.getModel();
            model.setRowCount(0);

            String countSql = "SELECT COUNT(*) FROM NGUOIHOC";
            Session session = HibernateUtil.getFactory().openSession();
            Transaction tr = session.beginTransaction();
            int rowCount = (Integer) session.createNativeQuery(countSql).uniqueResult();
            tr.commit();
            session.close();

            int totalPages = (int) Math.ceil((double) rowCount / limit);

            String dataSql = "SELECT * FROM NGUOIHOC ORDER BY MANH "
                    + "OFFSET :offset ROWS "
                    + "FETCH NEXT :limit ROWS ONLY";
            session = HibernateUtil.getFactory().openSession();
            tr = session.beginTransaction();
            SQLQuery query = session.createSQLQuery(dataSql)
                    .addEntity(NguoiHoc.class)
                    .setParameter("limit", limit)
                    .setParameter("offset", offset);
            nguoiHocList = query.list();
            fillTable(nguoiHocList);
            tr.commit();
            session.close();

            pagination1.setPagegination(page, totalPages);
            System.out.println("Tổng số trang: " + totalPages);
            System.out.println("Trang hiện tại: " + page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        loadData(1);
    }//GEN-LAST:event_formWindowOpened

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        NguoiHoc nh = this.readForm();
        if (validateData()) {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn thêm người học ? ", "Thêm người học", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                if (nhsv.add(nh) != 0) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công");
                    fillTable(nguoiHocList);
                }
            }
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void jtbNguoiHocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbNguoiHocMouseClicked
        index = jtbNguoiHoc.getSelectedRow();
        showData(index);
    }//GEN-LAST:event_jtbNguoiHocMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NguoiHocView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NguoiHocView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NguoiHocView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NguoiHocView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NguoiHocView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.maven.swing.Button btnHide;
    private com.maven.swing.Button btnSua;
    private com.maven.swing.Button btnThem;
    private com.maven.swing.Button btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtbNguoiHoc;
    private com.maven.swing.Pagination pagination1;
    private com.maven.swing.RadioButtonCustom rdNam;
    private com.maven.swing.RadioButtonCustom rdNu;
    private com.maven.swing.TextArea textArea1;
    private com.maven.swing.TextAreaScroll textAreaScroll1;
    private com.maven.swing.TextField txtEmail;
    private com.maven.swing.TextArea txtGhiChu;
    private com.maven.swing.TextField txtMaNguoiHoc;
    private com.maven.swing.TextField txtMaNhanVien;
    private com.maven.swing.PasswordField txtMatKhau;
    private com.toedter.calendar.JDateChooser txtNgayDangKi;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private com.maven.swing.TextField txtSoDienThoai;
    // End of variables declaration//GEN-END:variables
}
