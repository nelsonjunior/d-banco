import http from 'k6/http';
import {check, sleep} from 'k6';

const params = {
    headers: {
        'Content-Type': 'application/json',
    },
};

const data = JSON.parse(open('data/setup-data.json'));

function getRandon(max) {
    return Math.floor(Math.random() * max) + 1;
}

export function setup() {

    console.log('Setup Data');

    const urlCadastro = 'http://localhost:8081/v1/contas';
    const urlDeposito = 'http://localhost:8082/v1/contas/depositar';

    for (let i = 0; i < data.length; i++) {

        var payload = {
            "cpf": data[i].cpf,
            "valor": getRandon(10)
        };

        http.post(urlCadastro, JSON.stringify(data[i]), params);
        sleep(0.2);
        http.post(urlDeposito, JSON.stringify(payload), params);

    }

}

export default function() {

    const url = `http://localhost:8083/v1/contas/${data[getRandon(6)].cpf}`;

    const res = http.get(url, params);

    check(res, { 'Saldo obtido com sucesso': (r) => r.status === 200 });
}