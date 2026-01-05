INSERT INTO garden (id, name) VALUES
(1, 'Garden A'),
(2, 'Garden B');

INSERT INTO sensor_data (id, sensor_type, garden_id, value, updated_at) VALUES
(1, 'TEMPERATURE', 1, 22.5, '2025-12-17 08:00:00'),
(2, 'HUMIDITY', 1, 45.0, '2025-12-17 08:00:00'),
(3, 'LIGHT', 1, 320.5, '2025-12-17 07:30:00'),
(4, 'SOIL_MOISTURE', 1, 35.6, '2025-12-17 08:40:00');


-- FIX: bảng device KHÔNG có cột device_code
INSERT INTO device (id, device_name, garden_id, status, gpio) VALUES
(1, 'Water Pump', 1, 'OFF', 18),
(2, 'Garden Light', 1, 'OFF', 19);

INSERT INTO device_log (device_id, status, action_at) VALUES
(1, 'ON',  '2025-12-17 06:00:00'),
(1, 'OFF', '2025-12-17 06:30:00'),
(2, 'ON',  '2025-12-17 18:00:00'),
(2, 'OFF', '2025-12-17 22:00:00');

INSERT INTO scheduler
(id, device_id, enabled, repeat_type, on_time, off_time, executed)
VALUES
(
    1,
    1,
    true,
    'ONCE',
    '2025-12-17T06:00:00',
    '2025-12-17T06:30:00',
    false
);
