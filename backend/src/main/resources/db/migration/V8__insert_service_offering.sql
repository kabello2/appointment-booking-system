INSERT INTO service_offerings (
    id,
    service_code,
    name,
    description,
    duration_minutes,
    active,
    created_at,
    created_by,
    updated_at,
    updated_by,
    version
)
VALUES

    (
        gen_random_uuid(),
        'SRV001',
        'Home Loan',
        'Home loan applications, affordability assessments and mortgage consultations.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV002',
        'Vehicle Finance',
        'Vehicle finance applications for new and pre-owned vehicles.',
        45,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV003',
        'Personal Loan',
        'Personal lending applications and credit assessments.',
        45,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV004',
        'Business Loan',
        'Business funding and financing solutions for SMEs and enterprises.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV005',
        'Savings Account',
        'Savings account opening and financial wellness guidance.',
        30,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV006',
        'Current Account',
        'Personal and business current account services.',
        30,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV007',
        'Fixed Deposit Investment',
        'Fixed-term deposit investment products and consultation.',
        45,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV008',
        'Unit Trust Investment',
        'Investment planning and unit trust advisory services.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV009',
        'Retirement Planning',
        'Retirement annuities, pension preservation and retirement planning.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV010',
        'Credit Card Services',
        'Credit card applications, upgrades and account support.',
        30,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV011',
        'Debt Consolidation',
        'Debt review alternatives and debt consolidation financing.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV012',
        'Insurance Consultation',
        'Life, vehicle and household insurance advisory services.',
        45,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV013',
        'Forex Services',
        'Foreign exchange, international payments and travel cards.',
        30,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV014',
        'Private Banking',
        'Private banking and wealth management consultations.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV015',
        'Business Banking',
        'Business account management and banking solutions.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV016',
        'Asset Finance',
        'Commercial asset finance for equipment and machinery.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV017',
        'Student Loan',
        'Educational funding and student finance services.',
        45,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    ),

    (
        gen_random_uuid(),
        'SRV018',
        'Estate Planning',
        'Estate administration, trusts and wealth transfer planning.',
        60,
        true,
        NOW(),
        'SYSTEM',
        NOW(),
        'SYSTEM',
        0
    );