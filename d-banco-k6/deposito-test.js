import http from 'k6/http';
import {check, sleep} from 'k6';

const incomePayload = JSON.parse(open("data/deposito-data.json"));

export default function() {
    const url = 'http://localhost:8082/v1/contas/depositar';

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, JSON.stringify(incomePayload), params);

    check(res, { 'deposito realizado com sucesso': (r) => r.status === 201 });
}