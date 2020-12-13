import http from 'k6/http';
import {check, sleep} from 'k6';

const params = {
    headers: {
        'Content-Type': 'application/json',
    },
};

const data = JSON.parse(open('data/setup-depositar-data.json'));

function getRandon(max) {
    return Math.floor(Math.random() * max) + 1;
}

export function setup() {

    console.log('Setup Data');

    const urlCadastro = 'http://localhost:8081/v1/contas';

    for (let i = 0; i < data.length; i++) {
        http.post(urlCadastro, JSON.stringify(data[i]), params);
    }

    sleep(2);

}

export default function() {

    const urlDeposito = 'http://localhost:8082/v1/contas/depositar';

    var payload = {
        "cpf": data[getRandon(49)].cpf,
        "valor": getRandon(10)
    };

    const res =  http.post(urlDeposito, JSON.stringify(payload), params);

    check(res, { 'Deposito realizado com sucesso': (r) => r.status === 201 });
}