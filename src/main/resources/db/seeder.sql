-- ============================================================
-- SKYERP DATA SEEDER - 2026 Comprehensive Edition (FIXED v2)
-- PostgreSQL Optimized with 63 Tables mapped to Backend Entities
-- ============================================================

-- Disable FK checks temporarily for TRUNCATE
SET session_replication_role = 'replica';

-- TRUNCATE all 63 tables identified in Backend Models
TRUNCATE TABLE
    approval_configurations, audit_logs, 
    depreciation_histories, fixed_assets,
    journal_details, journal_headers,
    project_tasks, project_budgets, projects,
    stock_adjustment_items, stock_adjustments,
    stock_movements, stock_opname_items, stock_opnames,
    stock_transfer_items, stock_transfers,
    work_order_items, work_orders,
    inventories,
    goods_receipt_items, goods_receipts,
    purchase_return_items, purchase_returns,
    purchase_order_items, purchase_orders,
    purchase_request_items, purchase_requests,
    supplier_payments, purchase_invoices,
    sales_return_items, sales_returns,
    delivery_order_items, delivery_orders,
    payments, invoices,
    sales_order_items, sales_orders,
    quotation_items, quotations,
    price_list_items, price_lists,
    bom_items, boms,
    budget_items, budgets,
    notifications,
    timesheets,
    taxes, banks,
    chart_of_accounts,
    warehouses,
    customers, suppliers,
    products, categories, uom,
    employees,
    users,
    positions, departments, offices, organizations,
    services
RESTART IDENTITY CASCADE;

-- Re-enable FK checks
SET session_replication_role = 'origin';

-- =====================================================
-- 1. ORGANIZATIONS
-- =====================================================
INSERT INTO organizations (id, code, name, address, phone, email, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'ORG-001','PT Maju Jaya Utama','Jakarta Selatan','021-123456','admin@majujaya.com',true,'system',NOW()),
(2,'ORG-002','PT Sinar Global','Surabaya','031-654321','admin@sinarglobal.com',true,'system',NOW()),
(3,'ORG-003','CV Berkah Abadi','Bandung','022-112233','admin@berkah.com',true,'system',NOW()),
(4,'ORG-004','UD Karya Mandiri','Semarang','024-445566','admin@karya.com',true,'system',NOW()),
(5,'ORG-005','PT Nusantara Tech','Medan','061-778899','admin@nutech.com',true,'system',NOW());
SELECT setval('organizations_id_seq', 5);

-- =====================================================
-- 2. OFFICES (No active column)
-- =====================================================
INSERT INTO offices (id, code, name, address, phone, email, organization_id, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'OFF-JKT','Head Office Jakarta','Sudirman Central Business District', '021-111', 'jkt@skyerp.com', 1, 'system', NOW()),
(2,'OFF-SBY','Branch Surabaya','Rungkut Industri', '031-222', 'sby@skyerp.com', 2, 'system', NOW()),
(3,'OFF-BDG','Branch Bandung','Dago Atas', '022-333', 'bdg@skyerp.com', 3, 'system', NOW()),
(4,'OFF-SMG','Branch Semarang','Simpang Lima', '024-444', 'smg@skyerp.com', 4, 'system', NOW()),
(5,'OFF-MDN','Branch Medan','Jatinegara', '061-555', 'mdn@skyerp.com', 5, 'system', NOW());
SELECT setval('offices_id_seq', 5);

-- =====================================================
-- 3. DEPARTMENTS (No active column)
-- =====================================================
INSERT INTO departments (id, code, name, office_id, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'DEPT-FIN','Finance & Accounting',1,'system',NOW()),
(2,'DEPT-PUR','Purchasing',1,'system',NOW()),
(3,'DEPT-SAL','Sales & Marketing',1,'system',NOW()),
(4,'DEPT-WH','Warehouse',2,'system',NOW()),
(5,'DEPT-MFG','Manufacturing',2,'system',NOW());
SELECT setval('departments_id_seq', 5);

-- =====================================================
-- 4. POSITIONS (No active column)
-- =====================================================
INSERT INTO positions (id, code, title, department_id, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'POS-MGR','Manager',1,'system',NOW()),
(2,'POS-SUP','Supervisor',2,'system',NOW()),
(3,'POS-STF','Staff',3,'system',NOW()),
(4,'POS-ADM','Admin',4,'system',NOW()),
(5,'POS-OPR','Operator',5,'system',NOW());
SELECT setval('positions_id_seq', 5);

-- =====================================================
-- 5. USERS (No active column, pass: password)
-- =====================================================
INSERT INTO users (id, username, full_name, password, email, role, organization_id, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'admin','Super Admin','$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW','admin@erp.com','SUPER_ADMIN',1,'system',NOW()),
(2,'finance','Finance User','$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW','finance@erp.com','FINANCE',1,'system',NOW()),
(3,'purchasing','Purchasing User','$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW','purchasing@erp.com','PURCHASING',1,'system',NOW()),
(4,'sales','Sales User','$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW','sales@erp.com','SALES',1,'system',NOW()),
(5,'warehouse','Warehouse User','$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW','warehouse@erp.com','WAREHOUSE',1,'system',NOW());
SELECT setval('users_id_seq', 5);

-- =====================================================
-- 6. EMPLOYEES
-- =====================================================
INSERT INTO employees (id, user_id, employee_code, first_name, last_name, join_date, department_id, position_id, email, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,1,'EMP001','Super','Admin','2020-01-01',1,1,'admin@erp.com','system',NOW()),
(2,2,'EMP002','Finance','Staff','2021-02-01',1,3,'finance@erp.com','system',NOW()),
(3,3,'EMP003','Purchasing','Staff','2021-03-01',2,3,'purchasing@erp.com','system',NOW()),
(4,4,'EMP004','Sales','Staff','2021-04-01',3,3,'sales@erp.com','system',NOW()),
(5,5,'EMP005','Warehouse','Staff','2021-05-01',4,4,'warehouse@erp.com','system',NOW());
SELECT setval('employees_id_seq', 5);

-- =====================================================
-- 7. CATEGORIES & UOM & TAXES
-- =====================================================
INSERT INTO categories (id, code, name, active, created_by, created_at) OVERRIDING SYSTEM VALUE VALUES
(1,'CAT-FIN','Finished Goods',true,'admin',NOW()),
(2,'CAT-RAW','Raw Materials',true,'admin',NOW()),
(3,'CAT-SRV','Services',true,'admin',NOW()),
(4,'CAT-PRT','Spare Parts',true,'admin',NOW()),
(5,'CAT-GEN','General Supplies',true,'admin',NOW());
SELECT setval('categories_id_seq', 5);

INSERT INTO uom (id, code, name, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PCS','Pieces','admin',NOW()),
(2,'BOX','Box','admin',NOW()),
(3,'KG','Kilogram','admin',NOW()),
(4,'LTR','Liter','admin',NOW()),
(5,'MT','Meter','admin',NOW());
SELECT setval('uom_id_seq', 5);

INSERT INTO taxes (id, code, name, rate, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PPN11','PPN 11%',11.0,true,'admin',NOW()),
(2,'PPN00','PPN 0%',0.0,true,'admin',NOW()),
(3,'PPH23','PPH 23 (2%)',2.0,true,'admin',NOW()),
(4,'PPH21','PPH 21 (Custom)',5.0,true,'admin',NOW()),
(5,'TAX-FREE','Free Tax',0.0,true,'admin',NOW());
SELECT setval('taxes_id_seq', 5);

-- =====================================================
-- 8. PRODUCTS
-- =====================================================
INSERT INTO products (id, code, barcode, name, description, category_id, uom_id, cost_price, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PRD-LAP','BC-001','Laptop X1 Carbon','High-end business laptop',1,1,18000000,true,'admin',NOW()),
(2,'PRD-MOU','BC-002','Logitech MX Master','Wireless mouse',1,1,1200000,true,'admin',NOW()),
(3,'PRD-RAM','BC-003','DDR4 RAM 16GB','Memory module',2,1,800000,true,'admin',NOW()),
(4,'PRD-HDD','BC-004','SSD NVMe 1TB','Storage device',2,1,1500000,true,'admin',NOW()),
(5,'PRD-CLEAN','BC-005','Cleaning Kit','Screen cleaning solution',5,1,50000,true,'admin',NOW());
SELECT setval('products_id_seq', 5);

-- =====================================================
-- 9. SUPPLIERS & CUSTOMERS & WAREHOUSES
-- =====================================================
INSERT INTO suppliers (id, code, name, email, phone, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'SUP-001','PT Tech Distribution','sales@techdist.com','08111',true,'admin',NOW()),
(2,'SUP-002','CV Global Parts','sales@globalparts.com','08222',true,'admin',NOW()),
(3,'SUP-003','PT Sinar Logistik','ops@sinarlog.com','08333',true,'admin',NOW()),
(4,'SUP-004','UD Mandiri Jaya','sales@mandiri.com','08444',true,'admin',NOW()),
(5,'SUP-005','PT Mega Elektronik','mega@elek.com','08555',true,'admin',NOW());
SELECT setval('suppliers_id_seq', 5);

INSERT INTO customers (id, code, name, email, phone, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'CUST-001','Bank Central Asia','procurement@bca.co.id','021-BCA',true,'admin',NOW()),
(2,'CUST-002','PT Telkom Indonesia','sales@telkom.id','021-TLK',true,'admin',NOW()),
(3,'CUST-003','Shopee Int','ops@shopee.com','021-SHO',true,'admin',NOW()),
(4,'CUST-004','Grab Indonesia','tech@grab.com','021-GRB',true,'admin',NOW()),
(5,'CUST-005','Internal SkyERP','it@skyerp.id',NULL,true,'admin',NOW());
SELECT setval('customers_id_seq', 5);

INSERT INTO warehouses (id, code, name, address, organization_id, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'WH-MAIN','Main Warehouse','Cikarang Industrial Estate',1,'admin',NOW()),
(2,'WH-RAW','Raw Material Storage','Karawang Block A',1,'admin',NOW()),
(3,'WH-FIN','Finished Goods Store','Jakarta Hub',1,'admin',NOW()),
(4,'WH-SCRAP','Scrap Yard','Backyard',1,'admin',NOW()),
(5,'WH-TRANSIT','Transit Station','Dock Area',1,'admin',NOW());
SELECT setval('warehouses_id_seq', 5);

-- =====================================================
-- 10. ACCOUNTING (COA & BANKS)
-- =====================================================
INSERT INTO chart_of_accounts (id, code, name, type, level, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'101001','Cash on Hand','ASSET',1,true,'admin',NOW()),
(2,'101002','Bank BCA Operating','ASSET',1,true,'admin',NOW()),
(3,'102001','Accounts Receivable','ASSET',1,true,'admin',NOW()),
(4,'201001','Accounts Payable','LIABILITY',1,true,'admin',NOW()),
(5,'401001','Sales Revenue','REVENUE',1,true,'admin',NOW());
SELECT setval('chart_of_accounts_id_seq', 5);

INSERT INTO banks (id, code, name, account_number, branch, account_holder, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'BCA','Bank BCA','12345678','Sudirman','PT Maju Jaya Utama',true,'admin',NOW()),
(2,'MANDIRI','Bank Mandiri','88776655','Thamrin','PT Maju Jaya Utama',true,'admin',NOW()),
(3,'BNI','Bank BNI','11223344','Juanda','PT Maju Jaya Utama',true,'admin',NOW()),
(4,'BRI','Bank BRI','99001122','Bandung','PT Maju Jaya Utama',true,'admin',NOW()),
(5,'CIMB','CIMB Niaga','55667788','Karawaci','PT Maju Jaya Utama',true,'admin',NOW());
SELECT setval('banks_id_seq', 5);

-- =====================================================
-- 11. PURCHASING FLOW
-- =====================================================
INSERT INTO purchase_requests (id, document_number, trx_date, required_date, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PR-2403-001',NOW(),NOW() + interval '7 days','APPROVED','staff1',NOW()),
(2,'PR-2403-002',NOW(),NOW() + interval '5 days','PENDING','staff1',NOW()),
(3,'PR-2403-003',NOW(),NOW() + interval '10 days','APPROVED','staff1',NOW()),
(4,'PR-2403-004',NOW(),NOW() + interval '3 days','REJECTED','staff1',NOW()),
(5,'PR-2403-005',NOW(),NOW() + interval '14 days','DRAFT','staff1',NOW());
SELECT setval('purchase_requests_id_seq', 5);

INSERT INTO purchase_orders (id, document_number, trx_date, status, purchase_request_id, supplier_id, created_by_user_id, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PO-2403-001',NOW(),'APPROVED',1,1,3,NOW()),
(2,'PO-2403-002',NOW(),'APPROVED',3,2,3,NOW()),
(3,'PO-2403-003',NOW(),'DRAFT',NULL,1,3,NOW()),
(4,'PO-2403-004',NOW(),'PENDING',NULL,3,3,NOW()),
(5,'PO-2403-005',NOW(),'CLOSED',NULL,5,3,NOW());
SELECT setval('purchase_orders_id_seq', 5);

INSERT INTO goods_receipts (id, code, purchase_order_id, warehouse_id, date, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'GR-2403-001',1,1,NOW(),'VERIFIED','admin',NOW()),
(2,'GR-2403-002',2,2,NOW(),'VERIFIED','admin',NOW()),
(3,'GR-2403-003',5,3,NOW(),'DRAFT','admin',NOW()),
(4,'GR-2403-004',1,1,NOW(),'CANCELLED','admin',NOW()),
(5,'GR-2403-005',1,1,NOW(),'VERIFIED','admin',NOW());
SELECT setval('goods_receipts_id_seq', 5);

INSERT INTO purchase_invoices (id, code, purchase_order_id, supplier_id, date, due_date, status, total_amount, paid_amount, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PI-2403-001',1,1,CURRENT_DATE,CURRENT_DATE+30,'PAID',25000000,25000000,'admin',NOW()),
(2,'PI-2403-002',2,2,CURRENT_DATE,CURRENT_DATE+30,'UNPAID',15000000,0,'admin',NOW()),
(3,'PI-2403-003',1,1,CURRENT_DATE,CURRENT_DATE+30,'PARTIAL',10000000,5000000,'admin',NOW()),
(4,'PI-2403-004',1,1,CURRENT_DATE,CURRENT_DATE-5,'OVERDUE',5000000,0,'admin',NOW()),
(5,'PI-2403-005',2,2,CURRENT_DATE,CURRENT_DATE+60,'DRAFT',8000000,0,'admin',NOW());
SELECT setval('purchase_invoices_id_seq', 5);

INSERT INTO supplier_payments (id, code, purchase_invoice_id, supplier_id, date, amount, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'SP-2403-001',1,1,NOW(),25000000,'APPROVED','finance',NOW()),
(2,'SP-2403-002',3,1,NOW(),5000000,'APPROVED','finance',NOW()),
(3,'SP-2403-003',2,2,NOW(),0,'DRAFT','finance',NOW()),
(4,'SP-2403-004',1,1,NOW(),1000000,'PENDING','finance',NOW()),
(5,'SP-2403-005',2,2,NOW(),500000,'REJECTED','finance',NOW());
SELECT setval('supplier_payments_id_seq', 5);

-- =====================================================
-- 12. SALES FLOW
-- =====================================================
INSERT INTO quotations (id, document_number, trx_date, valid_until, customer_id, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'QUO-2403-001',NOW(),NOW() + interval '30 days',1,'APPROVED','sales',NOW()),
(2,'QUO-2403-002',NOW(),NOW() + interval '30 days',2,'APPROVED','sales',NOW()),
(3,'QUO-2403-003',NOW(),NOW() + interval '30 days',3,'PENDING','sales',NOW()),
(4,'QUO-2403-004',NOW(),NOW() + interval '30 days',4,'DRAFT','sales',NOW()),
(5,'QUO-2403-005',NOW(),NOW() + interval '30 days',1,'REJECTED','sales',NOW());
SELECT setval('quotations_id_seq', 5);

INSERT INTO sales_orders (id, document_number, trx_date, customer_id, quotation_id, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'SO-2403-001',NOW(),1,1,'CONFIRMED','sales',NOW()),
(2,'SO-2403-002',NOW(),2,2,'CONFIRMED','sales',NOW()),
(3,'SO-2403-003',NOW(),3,NULL,'DRAFT','sales',NOW()),
(4,'SO-2403-004',NOW(),4,NULL,'CANCELLED','sales',NOW()),
(5,'SO-2403-005',NOW(),1,NULL,'CONFIRMED','sales',NOW());
SELECT setval('sales_orders_id_seq', 5);

INSERT INTO invoices (id, code, sales_order_id, customer_id, date, due_date, status, total_amount, paid_amount, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'INV-2403-001',1,1,NOW(),NOW()+30,'SENT',35000000,0,'sales',NOW()),
(2,'INV-2403-002',2,2,NOW(),NOW()+30,'PAID',12000000,12000000,'sales',NOW()),
(3,'INV-2403-003',5,1,NOW(),NOW()+30,'PARTIAL',10000000,2000000,'sales',NOW()),
(4,'INV-2403-004',1,1,NOW(),NOW()-10,'OVERDUE',5000000,0,'sales',NOW()),
(5,'INV-2403-005',1,1,NOW(),NOW()+45,'DRAFT',4000000,0,'sales',NOW());
SELECT setval('invoices_id_seq', 5);

-- =====================================================
-- 13. MANUFACTURING (BOM & WORK ORDERS)
-- =====================================================
INSERT INTO boms (id, code, product_id, name, active, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'BOM-LAP-V1',1,'Laptop Basic Config',true,'admin',NOW()),
(2,'BOM-MOU-V1',2,'Mouse Factory Spec',true,'admin',NOW()),
(3,'BOM-LAP-V2',1,'Laptop Advanced Config',true,'admin',NOW()),
(4,'BOM-GEN-01',5,'Basic Kit Mix',true,'admin',NOW()),
(5,'BOM-SRV-01',3,'Service Install',true,'admin',NOW());
SELECT setval('boms_id_seq', 5);

INSERT INTO work_orders (id, document_number, bom_id, quantity, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'WO-2403-001',1,50,'IN_PROGRESS','admin',NOW()),
(2,'WO-2403-002',2,200,'COMPLETED','admin',NOW()),
(3,'WO-2403-003',1,30,'PLANNED','admin',NOW()),
(4,'WO-2403-004',3,10,'DRAFT','admin',NOW()),
(5,'WO-2403-005',1,100,'CANCELLED','admin',NOW());
SELECT setval('work_orders_id_seq', 5);

-- =====================================================
-- 14. INVENTORY & STOCK
-- =====================================================
INSERT INTO inventories (product_id, warehouse_id, quantity, last_updated) VALUES
(1,1,15,NOW()),(2,1,45,NOW()),(3,2,100,NOW()),(4,2,80,NOW()),(5,3,200,NOW());

INSERT INTO stock_movements (id, product_id, warehouse_id, quantity, type, reference_number, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,1,1,10,'IN','GR-2403-001','admin',NOW()),
(2,1,1,-2,'OUT','SO-2403-001','admin',NOW()),
(3,2,1,50,'IN','GR-2403-002','admin',NOW()),
(4,3,2,100,'IN','INITIAL','admin',NOW()),
(5,4,2,80,'IN','INITIAL','admin',NOW());
SELECT setval('stock_movements_id_seq', 5);

-- =====================================================
-- 15. PROJECTS
-- =====================================================
INSERT INTO projects (id, code, name, start_date, status, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PRJ-01','Implementation CRM','2024-01-01','IN_PROGRESS','admin',NOW()),
(2,'PRJ-02','Office Expansion','2024-02-01','PLANNED','admin',NOW()),
(3,'PRJ-03','Cloud Migration','2024-03-15','IN_PROGRESS','admin',NOW()),
(4,'PRJ-04','Security Audit','2024-06-01','ON_HOLD','admin',NOW()),
(5,'PRJ-05','Customer Loyalty App','2024-05-01','FINISHED','admin',NOW());
SELECT setval('projects_id_seq', 5);

-- =====================================================
-- 16. APPROVAL & AUDIT & NOTIF
-- =====================================================
INSERT INTO approval_configurations (id, process_name, threshold_amount, required_role, created_by, created_date) OVERRIDING SYSTEM VALUE VALUES
(1,'PURCHASE_ORDER',10000000.00,'MANAGER','admin',NOW()),
(2,'PURCHASE_ORDER',50000000.00,'DIRECTOR','admin',NOW()),
(3,'SALES_QUOTATION',0.00,'SUPERVISOR','admin',NOW()),
(4,'JOURNAL_ENTRY',100000000.00,'DIRECTOR','admin',NOW()),
(5,'BUDGET_PLAN',5000000.00,'MANAGER','admin',NOW());
SELECT setval('approval_configurations_id_seq', 5);

INSERT INTO audit_logs (id, action, entity_name, entity_id, username, timestamp) OVERRIDING SYSTEM VALUE VALUES
(1,'LOGIN','User',1,'admin',NOW()),
(2,'INSERT','Product',1,'admin',NOW()),
(3,'UPDATE','PurchaseOrder',1,'purchasing',NOW()),
(4,'APPROVE','Quotation',1,'sales',NOW()),
(5,'POST','Journal',1,'finance',NOW());
SELECT setval('audit_logs_id_seq', 5);

INSERT INTO notifications (id, title, message, username, read, created_at) OVERRIDING SYSTEM VALUE VALUES
(1,'New Task','Assign to PO Approval','manager1',false,NOW()),
(2,'Stock Alert','Product Laptop Low Stock','warehouse',false,NOW()),
(3,'Payment Due','Invoice INV-001 overdue','finance',true,NOW()),
(4,'System Update','Server maintenance tonight','admin',false,NOW()),
(5,'New SO','Sales Order SO-001 confirmed','warehouse',false,NOW());
SELECT setval('notifications_id_seq', 5);

-- Final Check
COMMIT;
