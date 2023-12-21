-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.7.33 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table invntr.inventaris
CREATE TABLE IF NOT EXISTS `inventaris` (
  `Kode` varchar(25) NOT NULL,
  `Nama` varchar(50) NOT NULL,
  `Jumlah` int(5) NOT NULL,
  `Kategori` varchar(50) NOT NULL,
  `Tipe` varchar(10) NOT NULL,
  `HargaBeli` int(11) NOT NULL,
  `TahunBeli` int(11) NOT NULL,
  `Foto` varchar(100) NOT NULL,
  PRIMARY KEY (`Kode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table invntr.inventaris: ~3 rows (approximately)
/*!40000 ALTER TABLE `inventaris` DISABLE KEYS */;
INSERT INTO `inventaris` (`Kode`, `Nama`, `Jumlah`, `Kategori`, `Tipe`, `HargaBeli`, `TahunBeli`, `Foto`) VALUES
	('LC001', 'LCD Proyektor', 2, 'Elektronik', 'Baru', 5000000, 2020, 'lcd.jpg'),
	('MK001', 'Meja Kantor', 5, 'Perabotan', 'Baru', 1500000, 2020, 'meja.jpg'),
	('WF001', 'WiFi', 1, 'Elektronik', 'Baru', 965000, 2023, 'wifi.jpg');
/*!40000 ALTER TABLE `inventaris` ENABLE KEYS */;

-- Dumping structure for table invntr.maintenance
CREATE TABLE IF NOT EXISTS `maintenance` (
  `Kode` varchar(25) NOT NULL,
  `TanggalMaintenance` date NOT NULL,
  `VendorMaintenance` varchar(50) NOT NULL,
  `StaffPIC` varchar(50) NOT NULL,
  PRIMARY KEY (`Kode`),
  CONSTRAINT `FKINVENTORIS` FOREIGN KEY (`Kode`) REFERENCES `inventaris` (`Kode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table invntr.maintenance: ~3 rows (approximately)
/*!40000 ALTER TABLE `maintenance` DISABLE KEYS */;
INSERT INTO `maintenance` (`Kode`, `TanggalMaintenance`, `VendorMaintenance`, `StaffPIC`) VALUES
	('LC001', '2021-12-19', 'PT Bali Sejahtera', 'Putu'),
	('MK001', '2022-12-19', 'PT Bali Makmur', 'Kadek'),
	('WF001', '2023-11-12', 'PT Bali Teknologi', 'Made');
/*!40000 ALTER TABLE `maintenance` ENABLE KEYS */;

-- Dumping structure for table invntr.staff
CREATE TABLE IF NOT EXISTS `staff` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nama` varchar(50) NOT NULL,
  `Jabatan` varchar(50) NOT NULL,
  `Tipe` varchar(25) NOT NULL,
  `Gaji` int(11) NOT NULL,
  `TahunBergabung` int(11) NOT NULL,
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table invntr.staff: ~1 rows (approximately)
/*!40000 ALTER TABLE `staff` DISABLE KEYS */;
INSERT INTO `staff` (`Id`, `Nama`, `Jabatan`, `Tipe`, `Gaji`, `TahunBergabung`) VALUES
	(1, 'Kadek', 'OB', 'Pegawai Baru', 3000000, 2021);
/*!40000 ALTER TABLE `staff` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
