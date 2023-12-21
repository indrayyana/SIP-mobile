<?php

require_once 'koneksi.php';

$response = array();

// Jika ini adalah APIcall, maka eksekusi php beserta parameternya
if (isset($_GET['apicall'])) {
    switch ($_GET['apicall']) {
        // CRUD Inventory
        case 'insertDataInventory':
            require_once 'inventory/insertData.php';
            break;
        case 'updateDataInventory':
            require_once 'inventory/updateData.php';
            break;
        case 'deleteDataInventory':
            require_once 'inventory/deleteData.php';
            break;
        case 'loadDataInventory':
            require_once 'inventory/loadData.php';
            break;
        // CRUD Staff
        case 'insertDataStaff':
            require_once 'staff/insertData.php';
            break;
        case 'updateDataStaff':
            require_once 'staff/updateData.php';
            break;
        case 'deleteDataStaff':
            require_once 'staff/deleteData.php';
            break;
        case 'loadDataStaff':
            require_once 'staff/loadData.php';
            break;
        // Maintenance
        case 'insertDataMaintenance':
            require_once 'maintenance/insertData.php';
            break;
        case 'updateDataMaintenance':
            require_once 'maintenance/updateData.php';
            break;
        case 'loadDataMaintenance':
            require_once 'maintenance/loadData.php';
            break;
        default:
            // jika nilai apicall tidak di kenal
            $response['error'] = true;
            $response['message'] = "Invalid Operation Called";
            break;
    }
} else {
    // jika bukan apicall
    $response['error'] = true;
    $response['message'] = "Invalid API Call";
}

echo json_encode($response);

// fungsi cek apakah parameter nya ada
function isTheseParametersAvailable($params)
{
    foreach ($params as $param) {
        if (!isset($_POST[$param])) {
            return false;
        }
    }

    return true;
}

?>