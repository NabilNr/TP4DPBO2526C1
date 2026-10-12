import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PeopleMenu extends JFrame {
    // Komponen GUI
    public JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;

    // Komponen JRadioButton (Bonus +20)
    private JRadioButton lakiLakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private ButtonGroup jenisKelaminGroup; // Memastikan hanya 1 yang bisa dipilih

    private JButton addUpdateButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable peopleTable;
    private JLabel titleLabel;

    // Attribute penampung data & tracker
    private ArrayList<Person> listPerson;
    private int selectedIndex = -1;

    public PeopleMenu() {
        // Inisialisasi List
        listPerson = new ArrayList<>();

        // Grouping JRadioButton agar saling bersaing (hanya 1 terkonfirmasi pilih)
        jenisKelaminGroup = new ButtonGroup();
        jenisKelaminGroup.add(lakiLakiRadioButton);
        jenisKelaminGroup.add(perempuanRadioButton);

        // Isi data awal (dummy/hardcoded)
        populateList();

        // Tampilkan ke tabel
        peopleTable.setModel(setTable());

        // Style Title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // Options Combo Box Kategori
        String[] kategoriData = {"--- Pilih Kategori ---", "Mahasiswa", "Dosen", "Tenaga Pendidik", "OB"};
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Sembunyikan tombol Delete di awal
        deleteButton.setVisible(false);

        // Listener: Tombol Add / Update
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Listener: Tombol Delete (dengan Confirmation Prompt)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pop-up Konfirmasi Hapus Data
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // Listener: Tombol Cancel
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Listener: Klik Baris Tabel
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = peopleTable.getSelectedRow();

                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curTahunLahir = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curJenisKelamin = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();

                idField.setText(curId);
                namaField.setText(curNama);
                tahunLahirField.setText(curTahunLahir);
                kategoriComboBox.setSelectedItem(curKategori);

                // Set RadioButton sesuai value dari tabel
                if (curJenisKelamin.equalsIgnoreCase("Laki-laki")) {
                    lakiLakiRadioButton.setSelected(true);
                } else if (curJenisKelamin.equalsIgnoreCase("Perempuan")) {
                    perempuanRadioButton.setSelected(true);
                } else {
                    jenisKelaminGroup.clearSelection();
                }

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    // Mengambil nilai string dari Radio Button yang dipilih
    private String getSelectedJenisKelamin() {
        if (lakiLakiRadioButton.isSelected()) {
            return "Laki-laki";
        } else if (perempuanRadioButton.isSelected()) {
            return "Perempuan";
        }
        return "-";
    }

    // Mengisi data awal
    public void populateList() {
        listPerson.add(new Person("2511372", "Khalifa Nabil Nur", 2007, "Mahasiswa", "Laki-laki"));
        listPerson.add(new Person("0418098102", "Rosa Ariani Sukamto", 1981, "Dosen", "Perempuan"));
        listPerson.add(new Person("123456", "Saepul Anwar", 1995, "Tenaga Pendidik", "Laki-laki"));
        listPerson.add(new Person("789100", "Cinta Lestari", 1998, "Dosen", "Perempuan"));
        listPerson.add(new Person("1782932", "Ahmad Fauzi", 2002, "OB", "Laki-laki"));
    }

    // Membentuk data model tabel
    public DefaultTableModel setTable() {
        Object[] cols = {"No", "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin"};
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        for (int i = 0; i < listPerson.size(); i++) {
            Object[] row = {
                    (i + 1),
                    listPerson.get(i).getId(),
                    listPerson.get(i).getNama(),
                    listPerson.get(i).getTahunLahir(),
                    listPerson.get(i).getKategori(),
                    listPerson.get(i).getJenisKelamin()
            };
            tmp.addRow(row);
        }
        return tmp;
    }

    // Operasi Create
    public void insertData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String jenisKelamin = getSelectedJenisKelamin();

            listPerson.add(new Person(id, nama, tahunLahir, kategori, jenisKelamin));
            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Operasi Update
    public void updateData() {
        try {
            String id = idField.getText();
            String nama = namaField.getText();
            int tahunLahir = Integer.parseInt(tahunLahirField.getText());
            String kategori = kategoriComboBox.getSelectedItem().toString();
            String jenisKelamin = getSelectedJenisKelamin();

            listPerson.get(selectedIndex).setId(id);
            listPerson.get(selectedIndex).setNama(nama);
            listPerson.get(selectedIndex).setTahunLahir(tahunLahir);
            listPerson.get(selectedIndex).setKategori(kategori);
            listPerson.get(selectedIndex).setJenisKelamin(jenisKelamin);

            peopleTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Operasi Delete
    public void deleteData() {
        listPerson.remove(selectedIndex);
        peopleTable.setModel(setTable());
        clearForm();

        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
    }

    // Reset Form
    public void clearForm() {
        idField.setText("");
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        jenisKelaminGroup.clearSelection();

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }

    // Main Method
    public static void main(String[] args) {
        PeopleMenu menu = new PeopleMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}